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

  // Takes a symbol as parameter, and inspect it to find all the interesting characteristic
  // necesarry to rebuild a mocked version of itself.
  private def buildOverrideType(symbol: Symbol): TypeRepr = 
    symbol.tree match
      case DefDef(_, Nil, returnTpt, _) =>
        // If the parameter list is empty, we are looking at a nullary function
        ByNameType(returnTpt.tpe)

      case DefDef(name, params, returnTpt, _) =>
        // safe because we have excluded the case where params is empty in an
        // earlier pattern match case.
        val head :: tail = params: @unchecked

        val iter = tail.iterator

        def transform(parent: List[PolyType]): ParamClause => TypeRepr = {
          case term: TermParamClause => 
            val names = term.params.map(_.name)
            val types = term.params.map { param =>

              val typeRepr = param.tpt.tpe  
              val typeName = typeRepr.show // Is there a better way to get the name ?

              // In the case of polymorphic function type, we should not refer to
              // the original type but to the one defined in our own PolyType. We do
              // so by keeping references to previously defined PolyType via parent.
              // Then on each term type, we look at that parent list and see if there
              // is a name that match, and if yes use the local type ref instead.
              // TODO Would it make sense to keep a list of (name -> TypeRepr) instead ?
              val fromParent = parent
                .zipWithIndex
                .flatMap { case (pt, outer) =>
                  pt.paramNames.zipWithIndex.map { (name, inner) => (outer, inner, name)}
                }
                .collectFirst {
                  case (outer, inner, name) if name == typeName => (outer, inner)
                }
              
              fromParent match
                case Some(outer -> inner) =>
                  parent(outer).param(inner)

                case None =>
                  typeRepr
            }
            
            MethodType(names)(_ => types,
            { mt =>
              if iter.hasNext then transform(parent)(iter.next()) else returnTpt.tpe
            })

          case TypeParamClause(typeDefs) => 
            // def apply(paramNames: List[String])(paramBoundsExp: PolyType => List[TypeBounds], resultTypeExp: PolyType => TypeRepr): PolyType
            println(s"type defs = $typeDefs")

            val paramNames = typeDefs.map(_.name)
            val trees = typeDefs.map(_.rhs)


            PolyType(paramNames)(
              pt => {
                // bounds is probably fine, except we need to not ignore the None case.
                // THat would move the bounds arround
                val bounds = trees
                  .flatMap {
                    case tt: TypeTree => Some(tt.tpe)
                    case tree => 
                      report.warning(s"Found a non-TypeTree when looking at type param rhs: $tree")
                      None
                  }
                  .flatMap {
                    case tb: TypeBounds => Some(tb)
                    case tpe =>
                      report.warning(s"Found a non-TypeBounds when looking at type param rhs: $tpe")
                      None
                  }
                  
                  println(s"type bounds = $bounds")
                  
                  trees.map(_ => TypeBounds(TypeRepr.of[Nothing], TypeRepr.of[Any]))
              }, 
              pt => if iter.hasNext then transform(pt :: parent)(iter.next()) else returnTpt.tpe
            )
        }

        transform(List.empty)(head)

      case tree =>
        report.error(s"unexpected tree: $tree")
        throw new UnsupportedOperationException(
          s"Unexpected tree found at symbol ${symbol.name}. "+
          "Report your use case to the library author."
        )

  // Given a symbol, return the MockFunction symbol as well as its type parameters
  private def buildMockFunctionType(symbol: Symbol): (Symbol, List[TypeRepr]) = 
    symbol.tree match
      case DefDef(_, Nil, returnTpt, _) =>
        // If the parameter list is empty, we are looking at a nullary function
        (Symbol.requiredClass("functions.MockFunction0"), List(returnTpt.tpe))

      case DefDef(name, params, returnTpt, _) =>
        val args = params.flatMap {
          case TermParamClause(vals) => vals
          case TypeParamClause(_) => List.empty
        }

        val types = args.map(_.tpt.tpe) :+ returnTpt.tpe

        (Symbol.requiredClass(s"functions.MockFunction${args.length}"), types)

      case tree =>
        report.error(s"unexpected tree: $tree")
        throw new UnsupportedOperationException(
          s"Unexpected tree found at symbol ${symbol.name}. "+
          "Report your use case to the library author."
        )

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

    val parents = if isTrait then List(TypeTree.of[Object], TypeTree.of[T], TypeTree.of[Mock])
    else List(TypeTree.of[T], TypeTree.of[Mock])

    def declarations(cls: Symbol): List[Symbol] =
      val mocks = Symbol.newVal(cls, "mocks", TypeRepr.of[Map[String, MockFunction]], Flags.Private, Symbol.noSymbol)
      val accessMockFunction = Symbol.newMethod(cls, "accessMockFunction", MethodType(List("name"))(_ => List(TypeRepr.of[String]), _ => TypeRepr.of[MockFunction]))

      val overrides = members.map { m =>
        
        // I don't know of a way to remove flags, so instead we have an allowlist of all
        // the known flags we want to keep. Let's see if that scale with the number of use case.
        val keepThoseFlags = Flags.Erased | Flags.Given | Flags.Implicit | Flags.Lazy | Flags.PrivateLocal
        
        val flags = (m.flags & keepThoseFlags) | Flags.Override
        val privateWithin = m.privateWithin.map(_.typeSymbol).getOrElse(Symbol.noSymbol)
        val tpe = buildOverrideType(m)
        
        Symbol.newMethod(cls, m.name, tpe, flags, privateWithin)
      }

      accessMockFunction :: mocks +: overrides

    val cls = Symbol.newClass(Symbol.spliceOwner, name, parents.map(_.tpe), declarations, selfType = None)

    // List all the symbols to override and associate them with the mock name.
    // We can't use the symbol name directly because overload would result with
    // multiple symbols having the same key.
    val clsMethodMembers = cls.methodMembers
    val overrides = clsMethodMembers
      .filter(sym => members.exists(_.name == sym.name))
      .zipWithIndex
      .map { case (sym, idx) =>
        // Let's find out if there are more than one method with the same name. If yes, we need
        // to change the mock name to distinguish between the various overload. We use the
        // position of the overload as such distinguisher. Note that the sort must be the same
        // at declaration time (here) and at invokation time (the when macro).
        val overload = utils.sortSymbolsViaSignature(clsMethodMembers.filter(_.name == sym.name))

        if overload.length == 1 then sym.name -> sym
        else {
          val idx = overload.indexWhere(_ == sym)

          s"${sym.name}-$idx" -> sym
        }
      }

    // Now that we have our symbols created, let's do the implementation


    // mocks map
    val tuplesAsExpression = Expr.ofSeq(buildMocksSeq(overrides, ctxTerm))
    val mocksValSym = cls.declaredField("mocks")
    val mocksVal = ValDef(mocksValSym, Some('{Map.from(${tuplesAsExpression})}.asTerm))

    // accessMockFunction def
    val accessMockFunctionSym = cls.declaredMethod("accessMockFunction").head
    val accessMockFunctionDef = DefDef(accessMockFunctionSym, {
      case List(List(arg: Term)) => Some(Apply(Select.unique(Ref(mocksValSym), "apply"), List(arg)))
      case args =>
        report.error(s"unexpected arguments received: ${args}")
        throw new UnsupportedOperationException("this is a bug. report your use case to the library author")
    })


    // and all overridden methods
    val overriddenMethodsDef = overrides.map { case (mockName, sym) =>
      val (mockFunctionClsSym, mockFnTypeArgs) = buildMockFunctionType(sym)

      DefDef(sym, {
        case argss =>
          val arguments = argss.flatten.flatMap {
            case t: Term => Some(t)
            case a => None // We can ignore the rest as we are only looking at arguments here
          }

          val mockFnType = AppliedType(mockFunctionClsSym.typeRef, mockFnTypeArgs)

          val stat1 = Select.unique(Ref(mocksValSym), "apply")
          val stat2 = Apply(stat1, List(Literal(StringConstant(mockName))))
          val stat3 = Select.unique(stat2, "asInstanceOf")
          val stat4 = TypeApply(stat3, List(Inferred(mockFnType)))
          val stat5 = Select.unique(stat4, "apply")
          val stat6 = Apply(stat5, arguments)

          Some(stat6)
      })
    }

    // And we can now wire the class definition together
    val clsDef = ClassDef(cls, parents, body = List(mocksVal, accessMockFunctionDef) ++ overriddenMethodsDef)
    val newCls = Typed(Apply(Select(New(TypeIdent(cls)), cls.primaryConstructor), Nil), TypeTree.of[T & Mock])
    val block = Block(List(clsDef), newCls)

    utils.debug(s"Generated code:")
    //utils.debug(s"Tree Structure: ${block.show(using Printer.TreeStructure)}")
    utils.debug(s"Tree Code: ${block.show(using Printer.TreeAnsiCode)}")

    block.asExprOf[T & Mock]

  private def buildMocksSeq(functionsToMock: List[(String, Symbol)], ctxTerm: Term): List[Expr[(String, MockFunction)]] =

    functionsToMock.map { case (mockName, sym) =>
      val (mockFunctionSym, mockFunctionTypeParams) = buildMockFunctionType(sym)
      val mockFunctionTerm = Ident(mockFunctionSym.termRef)

      val newMockFunction = TypeApply(
        Select(New(TypeIdent(mockFunctionSym)), mockFunctionSym.primaryConstructor),
        mockFunctionTypeParams.map(ref => Inferred(ref))
      )

      val createMF = Apply(newMockFunction, List(ctxTerm, Literal(StringConstant(sym.name))))

      val tuple2Sym = defn.TupleClass(2)

      val NewTuple = Select(New(TypeIdent(tuple2Sym)), tuple2Sym.primaryConstructor)
      val TypedNewTuple = TypeApply(
        NewTuple,
        List(Inferred(TypeRepr.of[String]), Inferred(TypeRepr.of[MockFunction]))
      )

      Apply(TypedNewTuple, List(Literal(StringConstant(mockName)), createMF)).asExprOf[Tuple2[String, MockFunction]]
    }

private[macros] object MockImpl:

  inline def apply[T](using ctx: MockContext): T & Mock = // TODO T & Mock (or do I need the second part?)
    ${impl[T]('ctx)}

  @experimental def impl[T](ctx: Expr[MockContext])(using quotes: Quotes)(using Type[T]): Expr[T & Mock] =
    new MockImpl[T](ctx).generate
