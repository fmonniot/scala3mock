package macros

import context.{Mock, MockContext}
import functions.{MockFunction, MockFunction1, MockFunction3}
import macros.WhenImpl.impl

import scala.annotation.experimental
import scala.quoted.*

// TODO Add a guard to fail compilation if T is higher kinded
//   This isn't supported, we need fully qualified type (the given type should be able to use
//   another type though)
/*
Some resources for macros:
- https://docs.scala-lang.org/scala3/guides/macros/macros.html
- https://docs.scala-lang.org/scala3/guides/macros/quotes.html
- https://github.com/softwaremill/tapir/blob/master/core/src/main/scala-3/sttp/tapir/macros/FormCodecMacros.scala
- https://softwaremill.com/scala-3-macros-tips-and-tricks
- https://github.com/lampepfl/dotty/blob/release-3.2.0/library/src/scala/quoted/Quotes.scala
*/
class MockImpl[T](ctx: Expr[MockContext])(using quotes: Quotes)(using Type[T]):
  import quotes.reflect.*

  @experimental def generate: Expr[T & Mock] =
    val ctxTerm = ctx.asTerm match
      case i: Inlined => i.body
      case _ =>
        throw new MatchError(s"The MockContext expression was not inlined. Report to the author.\n ctx = $ctx")

    val tType: TypeRepr = TypeRepr.of[T].dealias
    utils.debug(s"Mocking type $tType")

    val classSymbol = tType.classSymbol match
      case Some(sym) if sym.isClassDef => sym
      case maybeSymbol => throw new MatchError(s"Can only mock trait or class at the time ($maybeSymbol received)")

    val isTrait = classSymbol.flags.is(Flags.Trait)

    val objectMembers = Symbol.requiredClass("java.lang.Object").methodMembers
    val anyMembers = Symbol.requiredClass("scala.Any").methodMembers


    val members: List[Symbol] = classSymbol.methodMembers.filter { m =>
      !(objectMembers.contains(m) || anyMembers.contains(m))
    }

    // Start by declaring the "signature" of the class. That includes all its interfaces, but not the implementation
    val name: String = "mock" // TODO Add the mocked type as a suffix

    val parents = if(isTrait) List(TypeTree.of[Object], TypeTree.of[T], TypeTree.of[Mock])
    else List(TypeTree.of[T], TypeTree.of[Mock])

    val functionsToMock = members.map { sym =>
      val (tParams, terms) = sym.signature.paramSigs.partitionMap {
        case i: Int => Left(i)
        case s: String => Right(s)
      }

      FunctionToMock(sym.name, tParams.headOption, terms, sym.signature.resultSig)
    }

    def declarations(cls: Symbol): List[Symbol] =
      val mocks = Symbol.newVal(cls, "mocks", TypeRepr.of[Map[String, MockFunction]], Flags.Private, Symbol.noSymbol)
      val accessMockFunction = Symbol.newMethod(cls, "accessMockFunction", MethodType(List("name"))(_ => List(TypeRepr.of[String]), _ => TypeRepr.of[MockFunction]))

      val overrides = members.map { m =>
        // TODO Use the functionsToMock as a source instead?
        val signature: Signature = m.signature
        val (tParams, terms) = signature.paramSigs.partitionMap {
          case i: Int => Left(i)
          case s: String => Right(s)
        }

        val (argNames, argTypes) = terms.zipWithIndex.flatMap {
          case (tpe, i) => List(Left(s"arg$i"), Right(Symbol.requiredClass(tpe).typeRef))
        }.partitionMap(identity)

        val retType = Symbol.requiredClass(m.signature.resultSig).typeRef

        // TODO Doesn't support polymorphic functions, nor multiple argument lists
        val tpe = MethodType(argNames)(_ => argTypes, _ => retType)
        Symbol.newMethod(cls, m.name, tpe, flags = Flags.Override, Symbol.noSymbol)
      }

      accessMockFunction :: mocks +: overrides

    val cls = Symbol.newClass(Symbol.spliceOwner, name, parents.map(_.tpe), declarations, selfType = None)

    // Now that we have our symbols created, let's do the implementation


    // mocks map
    val tuplesAsExpression = Expr.ofSeq(buildMocksSeq(functionsToMock, ctxTerm))
    val mocksValSym = cls.declaredField("mocks")
    val mocksVal = ValDef(mocksValSym, Some('{Map.from(${tuplesAsExpression})}.asTerm))

    // accessMockFunction def
    val accessMockFunctionSym = cls.declaredMethod("accessMockFunction").head
    val accessMockFunctionDef = DefDef(accessMockFunctionSym, {
      case List(List(arg: Term)) => Some(Apply(Select.unique(Ref(mocksValSym), "apply"), List(arg)))
      case _ => throw new UnsupportedOperationException("report your use case to the library author")
    })

    // and all overridden methods
    val overriddenMethodsDef = functionsToMock.map {
      case FunctionToMock(name, _, paramTypes, resultSigType) =>
        val sym = cls.declaredMethod(name).head

        DefDef(sym, {
          case List(args) =>
            val termArgs = args.flatMap {
              case t: Term => Some(t)
              case a =>
                report.warning(s"Found a non-term in argument list: $a")
                None
            }

            val mockFunctionClsSym = Symbol.requiredClass(s"functions.MockFunction${paramTypes.length}").typeRef
            val mockFnTypeArgs = (paramTypes.map(t => Symbol.requiredClass(t).typeRef) :+ Symbol.requiredClass(resultSigType).typeRef)
            val mockFnType = AppliedType(mockFunctionClsSym, mockFnTypeArgs)

            val stat1 = Select.unique(Ref(mocksValSym), "apply")
            val stat2 = Apply(stat1, List(Literal(StringConstant(name))))
            val stat3 = Select.unique(stat2, "asInstanceOf")
            val stat4 = TypeApply(stat3, List(Inferred(mockFnType)))
            val stat5 = Select.unique(stat4, "apply")
            val stat6 = Apply(stat5, termArgs)

            Some(stat6)
          case _ => throw new UnsupportedOperationException("report your use case to the library author")
        })
    }

    // And we can now wire the class definition together
    val clsDef = ClassDef(cls, parents, body = List(mocksVal, accessMockFunctionDef) ++ overriddenMethodsDef)
    val newCls = Typed(Apply(Select(New(TypeIdent(cls)), cls.primaryConstructor), Nil), TypeTree.of[T & Mock])
    val block = Block(List(clsDef), newCls)

    utils.debug(s"Generated code:")
    utils.debug(s"Tree Structure: ${block.show(using Printer.TreeStructure)}")
    utils.debug(s"Tree Code: ${block.show(using Printer.TreeAnsiCode)}")

    block.asExprOf[T & Mock]

  private def buildMocksSeq(functionsToMock: List[FunctionToMock], ctxTerm: Term): List[Expr[(String, MockFunction)]] =

    functionsToMock.map { case FunctionToMock(name, typeParametersNum, paramTypes, resultSigType) =>
      val mockFunctionSym = Symbol.requiredClass(s"functions.MockFunction${paramTypes.length}")
      val mockFunctionTerm = Ident(mockFunctionSym.termRef) // TODO missing type parameters

      val newMockFunction = TypeApply(
        Select(New(TypeIdent(mockFunctionSym)), mockFunctionSym.primaryConstructor),
        (paramTypes.map(t => Symbol.requiredClass(t).typeRef) :+ Symbol.requiredClass(resultSigType).typeRef).map(ref => Inferred(ref))
      )

      val createMF = Apply(newMockFunction, List(ctxTerm, Literal(StringConstant(name))))

      val tuple2Sym = defn.TupleClass(2)

      val NewTuple = Select(New(TypeIdent(tuple2Sym)), tuple2Sym.primaryConstructor)
      val TypedNewTuple = TypeApply(
        NewTuple,
        List(Inferred(TypeRepr.of[String]), Inferred(TypeRepr.of[MockFunction]))
      )

      Apply(TypedNewTuple, List(Literal(StringConstant(name)), createMF)).asExprOf[Tuple2[String, MockFunction]]
    }

private[macros] object MockImpl:

  inline def apply[T](using ctx: MockContext): T & Mock = // TODO T & Mock (or do I need the second part?)
    ${impl[T]('ctx)}

  @experimental def impl[T](ctx: Expr[MockContext])(using quotes: Quotes)(using Type[T]): Expr[T & Mock] =
    new MockImpl[T](ctx).generate
