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
  // methodSymbol is a symbol on the method in the class we are mocking, not the symbol we are creating
  private def buildOverrideType(replacements: List[(Symbol, TypeRepr)], methodSymbol: Symbol): TypeRepr = 
    methodSymbol.tree match
      case DefDef(_, Nil, returnTpt, _) =>
        // If the parameter list is empty, we are looking at a nullary function
        ByNameType(returnTpt.tpe)

      case DefDef(name, head :: tail, returnTpt, _) =>
        val iter = tail.iterator

        // Helper to have a heterogeneous collection type. Unfortunately while we can now do
        // List[(Symbol | String, TypeRepr)], we still can't filter at runtime on the underlying
        // class. So instead we have List[(Par, TypeRepr)].
        enum Par {
          case Sym(sym: Symbol)
          case Str(str: String)

          def typeRef = this match
            case Sym(sym) => Some(sym.typeRef)
            case _ => None
        }
        object Par {
          def apply(name: String) = {
            val s = methodSymbol.typeMember(name)

            if s.isNoSymbol then Str(name) else Sym(s)
          }
        }

        /** Helper to substitute types */
        def substituteTypes(p: List[(Par, TypeRepr)])(tpe: TypeRepr) =    
            // Pass 1: replace all type symbols with their concrete implementation
            val (from, to) = p.collect { 
              case (Par.Sym(sym), r) if sym.typeRef =:= tpe => (sym, r)
            }.unzip

            // Replace references to type parameter of the class/trait with their concrete typea
            val tmp = tpe.substituteTypes(from, to)

            // Pass 2
            // Because there is no symbol for the type parameter we are creating
            // in the overriden method, we have to do an additional check. We are rendering
            // the type to a String, and if it matches one of the known local type parameter
            // (stored as Par.Str) then we replace it with the local type reference.
            def poorSubstitute(replacements: List[(String, TypeRepr)])(source: TypeRepr): TypeRepr =
              source match
                case TermRef(qual, name) => TermRef(poorSubstitute(replacements)(qual), name)
                case tr: TypeRef =>
                  replacements.find(_._1 == tr.show).fold(tr)(_._2)
                case AppliedType(tycon, args) =>
                  val a = poorSubstitute(replacements)(tycon)
                  val b = args.map(poorSubstitute(replacements))

                  AppliedType(a, b)

                case ThisType(repr) =>
                  poorSubstitute(replacements)(repr) // Are the ThisType import ?

                case TypeBounds(low, high) =>
                  TypeBounds(poorSubstitute(replacements)(low), poorSubstitute(replacements)(high))

                case other => other
            end poorSubstitute

            val fin = poorSubstitute(p.collect { case (Par.Str(s), tpe) => s -> tpe })(tmp)

            fin
        end substituteTypes

        // In the case of polymorphic function type, we should not refer to
        // the original type but to the one defined in our own PolyType. We do
        // so by keeping references to previously defined PolyType via parent.
        // Then on each term type, we look at that parent list and see if there
        // is a name that match, and if yes use the local type ref instead.
        def transform(parents: List[(Par, TypeRepr)]): ParamClause => TypeRepr =
          case term: TermParamClause =>
            val names = term.params.map(_.name)
            val types = term.params.map { param =>
              substituteTypes(parents)(param.tpt.tpe)
            }

            MethodType(names)(
              _ => types,
              mt => if iter.hasNext then transform(parents)(iter.next()) else {
                substituteTypes(parents)(returnTpt.tpe)
              }
            )

          case TypeParamClause(typeDefs) => 

            val paramNames = typeDefs.map(_.name)
            val trees = typeDefs.map(_.rhs)

            def parentsWithPolyType(pt: PolyType) =
              pt.paramNames.zipWithIndex.map { (name, idx) =>
                Par(name) -> pt.param(idx)  
              } ++ parents

            PolyType(paramNames)(
              pt => {
                val all = parentsWithPolyType(pt)

                // We have to substitute types within the bounds. We do a few type matching
                // keeping elements if they do not match. At the end, we substitute or
                // default to Nothing <: t <: Any if an error was found.
                val bounds = trees
                  .map {
                    case tt: TypeTree => Some(tt.tpe)
                    case tree => 
                      report.warning(s"Found a non-TypeTree when looking at type param rhs: $tree")
                      None
                  }
                  .flatMap {
                    case Some(tb: TypeBounds) => 
                      // The cast is safe because substituteTypes does not modify the type hierarchy
                      Some(substituteTypes(all)(tb).asInstanceOf[TypeBounds])
                    case tpe =>
                      report.warning(s"Found a non-TypeBounds when looking at type param rhs: $tpe")
                      Some(TypeBounds(TypeRepr.of[Nothing], TypeRepr.of[Any]))
                  }
                  
                  bounds
              }, 
              pt => {
                val all = parentsWithPolyType(pt)

                if !iter.hasNext then substituteTypes(all)(returnTpt.tpe) else {
                  transform(all)(iter.next())
                }
              }
            )
        end transform

        transform(replacements.map{ case (a, b) => Par.Sym(a) -> b})(head)

      case tree =>
        report.error(s"unexpected tree: $tree")
        report.errorAndAbort(
          s"Unexpected tree found at symbol ${methodSymbol.name}. "+
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

    // If the class/trait has type parameters, build a replacements list. It maps from the
    // type parameter to the concrete type. We need to reference the concrete one when implementing
    // methods or when declaring mocks.
    val replacements = if(tType.typeArgs.isEmpty) then List.empty else classSymbol.tree match
      case ClassDef(name, DefDef(_, params, _, _), _, _, _) =>
        println(s"Got ClassDef. name=$name")

        // List(TypeRef(ThisType(TypeRef(ThisType(TypeRef(NoPrefix,module class fixtures)),trait PolymorphicTrait)),type T))
        val typeParams = params.flatMap {
          case TypeParamClause(typeDefs) => typeDefs
          case _ => None
        }.map { case TypeDef(name, _) =>
          classSymbol.typeMember(name)
        }

        tType match
          case AppliedType(tycon, args) =>
            if (args.size != typeParams.size)
              report.errorAndAbort("Number of type params and concrete types isn't equal")

            typeParams.zip(args)

          case _ =>
            report.errorAndAbort("Found type with type parameters but given type isn't applied.")

      case tree =>
        report.errorAndAbort(s"Unsupported symbol tree. Expected TypeDef but got ${tree.toString().split("\\(")(0)}")

    val isTrait = classSymbol.flags.is(Flags.Trait)

    val objectMembers = Symbol.requiredClass("java.lang.Object").methodMembers
    val anyMembers = Symbol.requiredClass("scala.Any").methodMembers


    val methodsToOverride: List[Symbol] = classSymbol.methodMembers.filter { m =>
      !(objectMembers.contains(m) || anyMembers.contains(m))
    }

    val fieldsToOverride = classSymbol.fieldMembers.filter(_.flags.is(Flags.Deferred))

    // Start by declaring the "signature" of the class. That includes all its interfaces, but not the implementation
    val name: String = "mock" // TODO Add the mocked type as a suffix

    val parents = if isTrait then List(TypeTree.of[Object], TypeTree.of[T], TypeTree.of[Mock])
    else List(TypeTree.of[T], TypeTree.of[Mock])

    def declarations(cls: Symbol): List[Symbol] =
      val mocks = Symbol.newVal(cls, "mocks", TypeRepr.of[Map[String, MockFunction]], Flags.Private, Symbol.noSymbol)
      val accessMockFunction = Symbol.newMethod(cls, "accessMockFunction", MethodType(List("name"))(_ => List(TypeRepr.of[String]), _ => TypeRepr.of[MockFunction]))
        
      // I don't know of a way to remove flags, so instead we have an allowlist of all
      // the known flags we want to keep. Let's see if that scale with the number of use case.
      // Looks the compiler internal has &~ to remove a flag, but it is not exposed in the reflect API.
      val keepThoseFlags = Flags.Erased | Flags.Given | Flags.Implicit | Flags.Lazy | Flags.PrivateLocal

      val methodOverrides = methodsToOverride.map { m =>
        val flags = (m.flags & keepThoseFlags) | Flags.Override
        val privateWithin = m.privateWithin.map(_.typeSymbol).getOrElse(Symbol.noSymbol)
        val tpe = buildOverrideType(replacements, m)
        
        Symbol.newMethod(cls, m.name, tpe, flags, privateWithin)
      }

      val fieldOverrides = fieldsToOverride.map { f =>
        val privateWithin = f.privateWithin.map(_.typeSymbol).getOrElse(Symbol.noSymbol)

        val tpe = f.tree match 
          case ValDef(_, tpt, _) => tpt.tpe
          case other => throw new UnsupportedOperationException(s"Expected a ValDef when implementing fields but got $other")

        Symbol.newVal(cls, f.name, tpe, f.flags & keepThoseFlags, privateWithin)
      }

      (accessMockFunction :: mocks +: methodOverrides) ++ fieldOverrides

    val cls = Symbol.newClass(Symbol.spliceOwner, name, parents.map(_.tpe), declarations, selfType = None)

    // List all the symbols to override and associate them with the mock name.
    // We can't use the symbol name directly because overload would result with
    // multiple symbols having the same key.
    val clsMethodMembers = cls.methodMembers
    val methodsOverridesSymbols = clsMethodMembers
      .filter(sym => methodsToOverride.exists(_.name == sym.name))
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
    val tuplesAsExpression = Expr.ofSeq(buildMocksSeq(methodsOverridesSymbols, ctxTerm))
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


    // all overridden methods
    val overriddenMethodsDef = methodsOverridesSymbols.map { case (mockName, sym) =>
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

    // all fields implementation
    val valDefs = fieldsToOverride.map { sym =>
      val s = cls.fieldMember(sym.name)

      val value = if s.typeRef.<:<(TypeRepr.of[AnyRef]) then Literal(NullConstant()) else {
        Literal(s.typeRef.show match
          case "Byte" => ByteConstant(0)
          case "Short" => ShortConstant(0)
          case "Char" => CharConstant(0)
          case "Int" => IntConstant(0)
          case "Long" => LongConstant(0)
          case "Float" => FloatConstant(0.0f)
          case "Double" => DoubleConstant(0.0)
          case "Boolean" => BooleanConstant(false)
          case "Unit" => UnitConstant()
          case tpe => throw new UnsupportedOperationException(s"Unsupported type $tpe as val type")
        )
      }
      ValDef(s, Some( value ))
    }

    // And we can now wire the class definition together
    val clsDef = ClassDef(cls, parents, body = List(mocksVal, accessMockFunctionDef) ++ overriddenMethodsDef ++ valDefs)
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
        mockFunctionTypeParams.map(ref => Inferred(TypeRepr.of[Any]))
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
