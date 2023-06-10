package eu.monniot.scala3mock.macros

import eu.monniot.scala3mock.context.{Mock, MockContext}
import eu.monniot.scala3mock.functions.{
  MockFunction,
  MockFunction1,
  MockFunction3
}

import scala.annotation.experimental
import scala.quoted.*

private[scala3mock] object MockImpl:

  inline def apply[T](using ctx: MockContext): T & Mock =
    ${ impl[T]('ctx, debug = false) }

  // For debugging purposes, switch from apply to this one to get verbose (we mean it)
  // logs when generating the mocked class.
  inline def debug[T](using ctx: MockContext): T & Mock =
    ${ impl[T]('ctx, debug = true) }

  @experimental def impl[T](ctx: Expr[MockContext], debug: Boolean)(using
      quotes: Quotes
  )(using Type[T]): Expr[T & Mock] =
    new MockImpl[T](ctx, debug).generate

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
private class MockImpl[T](ctx: Expr[MockContext], debug: Boolean)(using
    quotes: Quotes
)(using Type[T]):
  import quotes.reflect.*

  def debug(x: String): Unit =
    if debug then println(x) else ()

  private def defaultLiteral(tpe: TypeRepr): Literal =
    if tpe <:< TypeRepr.of[AnyRef] then Literal(NullConstant())
    else if tpe =:= TypeRepr.of[Boolean] then Literal(BooleanConstant(false))
    else if tpe =:= TypeRepr.of[Byte] then Literal(ByteConstant(0))
    else if tpe =:= TypeRepr.of[Short] then Literal(ShortConstant(0))
    else if tpe =:= TypeRepr.of[Int] then Literal(IntConstant(0))
    else if tpe =:= TypeRepr.of[Long] then Literal(LongConstant(0))
    else if tpe =:= TypeRepr.of[Float] then Literal(FloatConstant(0))
    else if tpe =:= TypeRepr.of[Double] then Literal(DoubleConstant(0))
    else if tpe =:= TypeRepr.of[Char] then Literal(CharConstant(0))
    else if tpe =:= TypeRepr.of[String] then Literal(StringConstant(""))
    else if tpe =:= TypeRepr.of[Unit] then Literal(UnitConstant())
    else
      throw new UnsupportedOperationException(
        s"Don't know the default literal value of type $tpe."
      )

  // Given a symbol, return the MockFunction symbol as well as its type parameters
  private def buildMockFunctionType(symbol: Symbol): (Symbol, List[TypeRepr]) =
    symbol.tree match
      case DefDef(_, Nil, returnTpt, _) =>
        // If the parameter list is empty, we are looking at a nullary function
        (
          Symbol.requiredClass("eu.monniot.scala3mock.functions.MockFunction0"),
          List(returnTpt.tpe)
        )

      case DefDef(name, params, returnTpt, _) =>
        val args = params.flatMap {
          case TermParamClause(vals) => vals
          case TypeParamClause(_)    => List.empty
        }

        val types = args.map(_.tpt.tpe) :+ returnTpt.tpe

        (
          Symbol.requiredClass(
            s"eu.monniot.scala3mock.functions.MockFunction${args.length}"
          ),
          types
        )

      case tree =>
        report.error(s"unexpected tree: $tree")
        throw new UnsupportedOperationException(
          s"Unexpected tree found at symbol ${symbol.name}. " +
            "Report your use case to the library author."
        )

  private def buildMocksSeq(
      functionsToMock: List[(String, Symbol)],
      ctxTerm: Term
  ): List[Expr[(String, MockFunction)]] =
    functionsToMock.map { case (mockName, sym) =>
      val (mockFunctionSym, mockFunctionTypeParams) = buildMockFunctionType(sym)
      val mockFunctionTerm = Ident(mockFunctionSym.termRef)

      val newMockFunction = TypeApply(
        Select(
          New(TypeIdent(mockFunctionSym)),
          mockFunctionSym.primaryConstructor
        ),
        mockFunctionTypeParams.map(ref => Inferred(TypeRepr.of[Any]))
      )

      val createMF =
        Apply(newMockFunction, List(ctxTerm, Literal(StringConstant(sym.name))))

      val tuple2Sym = defn.TupleClass(2)

      val NewTuple =
        Select(New(TypeIdent(tuple2Sym)), tuple2Sym.primaryConstructor)
      val TypedNewTuple = TypeApply(
        NewTuple,
        List(Inferred(TypeRepr.of[String]), Inferred(TypeRepr.of[MockFunction]))
      )

      Apply(TypedNewTuple, List(Literal(StringConstant(mockName)), createMF))
        .asExprOf[Tuple2[String, MockFunction]]
    }

  @experimental def generate: Expr[T & Mock] =
    val ctxTerm = ctx.asTerm match
      case i: Inlined => i.body
      case _ =>
        throw new MatchError(
          s"The MockContext expression was not inlined. Report to the author.\n ctx = $ctx"
        )

    val tType: TypeRepr = TypeRepr.of[T].dealias
    debug(s"Mocking type $tType")

    val classSymbol = tType.classSymbol match
      case Some(sym) if sym.isClassDef => sym
      case maybeSymbol =>
        throw new MatchError(
          s"Can only mock trait or class at the time ($maybeSymbol received)"
        )

    val classDef = classSymbol.tree match
      case cd: ClassDef => cd
      case tree =>
        report.errorAndAbort(
          s"Unsupported symbol tree. Expected ClassDef but got ${tree.toString().split("\\(")(0)}"
        )

    // If the class/trait has type parameters, build a replacements list. It maps from the
    // type parameter to the concrete type. We need to reference the concrete one when implementing
    // methods or when declaring mocks.
    val replacements =
      if (tType.typeArgs.isEmpty) then List.empty
      else

        val typeParams = classDef.constructor.paramss
          .flatMap {
            case TypeParamClause(typeDefs) => typeDefs
            case _                         => None
          }
          .map { case TypeDef(name, _) =>
            classSymbol.typeMember(name)
          }

        tType match
          case AppliedType(tycon, args) =>
            if (args.size != typeParams.size)
              report.errorAndAbort(
                "Number of type params and concrete types isn't equal"
              )

            typeParams.zip(args)

          case _ =>
            report.errorAndAbort(
              "Found type with type parameters but given type isn't applied."
            )

    val isTrait = classSymbol.flags.is(Flags.Trait)

    val objectMembers = Symbol.requiredClass("java.lang.Object").methodMembers
    val anyMembers = Symbol.requiredClass("scala.Any").methodMembers

    val methodsToOverride: List[Symbol] = classSymbol.methodMembers
      .filter { m =>
        !(objectMembers.contains(m) || anyMembers.contains(m))
      }
      .filterNot(_.flags.is(Flags.Private)) // Do not override private members

    // fields are values. TIL that scala has val without implementation :)
    val fieldsToOverride =
      classSymbol.fieldMembers.filter(_.flags.is(Flags.Deferred))

    // Start by declaring the "signature" of the class. That includes all its interfaces, but not the implementation
    val name: String = s"${classDef.name}Mock"

    // When declaring a class symbol, we need the TypeTree of every parents
    val parentsTypes =
      if isTrait then
        List(TypeTree.of[Object], TypeTree.of[T], TypeTree.of[Mock])
      else List(TypeTree.of[T], TypeTree.of[Mock])

    // But when building the class definition, we need to use term if the mocked type T
    // needs to call the constructor of T with default values.
    val parentsTree: List[Tree] =
      val termParams = classDef.constructor.termParamss.map(_.params).flatten

      if termParams.isEmpty then parentsTypes
      else
        parentsTypes.map { tt =>
          if tt != TypeTree.of[T]
          then tt
          else
            // eg. T[x, y]
            val typeApply = TypeApply(
              Select(
                New(TypeTree.of[T]),
                classDef.constructor.symbol
              ),
              tType.typeArgs.map(Inferred(_))
            )

            // Apply the parameter list to build out the fully constructed parent
            classDef.constructor.paramss
              .collect { case TermParamClause(defs) =>
                defs.map(valDef => defaultLiteral(valDef.tpt.tpe))
              }
              .foldLeft[Term](typeApply) { case (a, b) =>
                Apply(a, b)
              }
        }

    def declarations(cls: Symbol): List[Symbol] =
      val mocks = Symbol.newVal(
        cls,
        "mocks",
        TypeRepr.of[Map[String, MockFunction]],
        Flags.Private,
        Symbol.noSymbol
      )
      val accessMockFunction = Symbol.newMethod(
        cls,
        "accessMockFunction",
        MethodType(List("name"))(
          _ => List(TypeRepr.of[String]),
          _ => TypeRepr.of[MockFunction]
        )
      )

      // I don't know of a way to remove flags, so instead we have an allowlist of all
      // the known flags we want to keep. Let's see if that scale with the number of use case.
      // Looks the compiler internal has &~ to remove a flag, but it is not exposed in the reflect API.
      val keepThoseFlags =
        Flags.Erased | Flags.Given | Flags.Implicit | Flags.Lazy | Flags.PrivateLocal

      val methodOverrides = methodsToOverride.map { m =>
        val flags = (m.flags & keepThoseFlags) | Flags.Override
        val privateWithin =
          m.privateWithin.map(_.typeSymbol).getOrElse(Symbol.noSymbol)

        val tpe = cls.typeRef.memberType(m)

        Symbol.newMethod(cls, m.name, tpe, flags, privateWithin)
      }

      val fieldOverrides = fieldsToOverride.map { f =>
        val privateWithin =
          f.privateWithin.map(_.typeSymbol).getOrElse(Symbol.noSymbol)

        val tpe = f.tree match
          case ValDef(_, tpt, _) => tpt.tpe
          case other =>
            throw new UnsupportedOperationException(
              s"Expected a ValDef when implementing fields but got $other"
            )

        Symbol.newVal(cls, f.name, tpe, f.flags & keepThoseFlags, privateWithin)
      }

      (accessMockFunction :: mocks +: methodOverrides) ++ fieldOverrides

    val cls = Symbol.newClass(
      Symbol.spliceOwner,
      name,
      parentsTypes.map(_.tpe),
      declarations,
      selfType = None
    )

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
        val overload = utils.sortSymbolsViaSignature(
          clsMethodMembers.filter(_.name == sym.name)
        )

        if overload.length == 1 then sym.name -> sym
        else {
          val idx = overload.indexWhere(_ == sym)

          s"${sym.name}-$idx" -> sym
        }
      }

    // Now that we have our symbols created, let's do the implementation

    // mocks map
    val tuplesAsExpression =
      Expr.ofSeq(buildMocksSeq(methodsOverridesSymbols, ctxTerm))
    val mocksValSym = cls.declaredField("mocks")
    val mocksVal =
      ValDef(mocksValSym, Some('{ Map.from(${ tuplesAsExpression }) }.asTerm))

    // accessMockFunction def
    val accessMockFunctionSym = cls.declaredMethod("accessMockFunction").head
    val accessMockFunctionDef = DefDef(
      accessMockFunctionSym,
      {
        case List(List(arg: Term)) =>
          Some(Apply(Select.unique(Ref(mocksValSym), "apply"), List(arg)))
        case args =>
          report.error(s"unexpected arguments received: ${args}")
          throw new UnsupportedOperationException(
            "this is a bug. report your use case to the library author"
          )
      }
    )

    // all overridden methods
    val overriddenMethodsDef = methodsOverridesSymbols.map {
      case (mockName, sym) =>
        val (mockFunctionClsSym, mockFnTypeArgs) = buildMockFunctionType(sym)

        DefDef(
          sym,
          { case argss =>
            val arguments = argss.flatten.flatMap {
              case t: Term => Some(t)
              case a =>
                None // We can ignore the rest as we are only looking at arguments here
            }

            val mockFnType =
              AppliedType(mockFunctionClsSym.typeRef, mockFnTypeArgs)

            val stat1 = Select.unique(Ref(mocksValSym), "apply")
            val stat2 = Apply(stat1, List(Literal(StringConstant(mockName))))
            val stat3 = Select.unique(stat2, "asInstanceOf")
            val stat4 = TypeApply(stat3, List(Inferred(mockFnType)))
            val stat5 = Select.unique(stat4, "apply")
            val stat6 = Apply(stat5, arguments)

            Some(stat6)
          }
        )
    }

    // all fields implementation
    val valDefs = fieldsToOverride.map { sym =>
      val s = cls.fieldMember(sym.name)

      ValDef(s, Some(defaultLiteral(s.typeRef)))
    }

    // And we can now wire the class definition together
    val clsDef = ClassDef(
      cls,
      parentsTree,
      body =
        List(mocksVal, accessMockFunctionDef) ++ overriddenMethodsDef ++ valDefs
    )
    val newCls = Typed(
      Apply(Select(New(TypeIdent(cls)), cls.primaryConstructor), Nil),
      TypeTree.of[T & Mock]
    )
    val block = Block(List(clsDef), newCls)

    debug(s"Generated code:")
    debug(s"Tree Code: ${block.show(using Printer.TreeAnsiCode)}")

    block.asExprOf[T & Mock]
  end generate
