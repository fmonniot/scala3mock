package eu.monniot.scala3mock.macros

import eu.monniot.scala3mock.context.{Mock, MockContext}
import eu.monniot.scala3mock.functions.{
  MockFunction,
  MockFunction1,
  MockFunction3
}

import scala.annotation.experimental
import scala.quoted.*
import eu.monniot.scala3mock.Default

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

  private def defaultValueForType(tpe: TypeRepr): Term =
    Implicits.search(TypeRepr.of[Default].appliedTo(tpe)) match {
      case e: ImplicitSearchSuccess =>
        // Here we are voluntarily not supporting classes that have
        // type parameters. We can do so because the Default type class
        // do not have any specific values with it, so it would default
        // to the AnyRef one (given[Y]). The issue with this one is that
        // we have to align the types, and I'm not entirely sure how to
        // do so. So for now we always return null and future us can worry
        // about this issue.
        if (tpe.typeArgs.isEmpty) Select.unique(e.tree, "default")
        else Literal(NullConstant())

      case e: ImplicitSearchFailure =>
        report.errorAndAbort(
          s"Couldn't find a Default instance for the type ${tpe}: ${e.explanation}"
        )
    }

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
        val args = params
          .flatMap {
            case TermParamClause(vals) => vals.map(_.tpt.tpe)
            case TypeParamClause(_)    => List.empty
          }
          .map {
            case ByNameType(inner) =>
              // By-name types are interesting because internally they have a type-representation,
              // but that is not something that we can actually do in "official" Scala. Not removing
              // this node means that the by-name value isn't being evaluated and so when we compare
              // the expectation (an evaluated value) vs the actual value (a lambda, as it's not evaluated)
              // we ends up with some mismatch. By removing this type component, we are effectively saying to
              // the compiler that we want to evaluate the argument before passing it to our MockFunction
              inner
            case otherwise => otherwise
          }

        (
          Symbol.requiredClass(
            s"eu.monniot.scala3mock.functions.MockFunction${args.length}"
          ),
          args :+ returnTpt.tpe
        )

      case tree =>
        report.error(s"unexpected tree: $tree")
        throw new UnsupportedOperationException(
          s"Unexpected tree found at symbol ${symbol.name}. " +
            "Report your use case to the library author."
        )

  private def buildMocksSeq(
      functionsToMock: List[(String, Symbol)],
      ctxTerm: Term,
      mockedClassName: String
  ): List[Expr[(String, MockFunction)]] =
    functionsToMock.map { case (mockName, sym) =>
      val (mockFunctionSym, mockFunctionTypeParams) = buildMockFunctionType(sym)

      val implicitDefaultType =
        TypeRepr.of[Default].appliedTo(mockFunctionTypeParams.last)
      val defaultTypeClass = Implicits.search(implicitDefaultType) match {
        case e: ImplicitSearchSuccess => e
        case e: ImplicitSearchFailure =>
          report.errorAndAbort(
            s"Couldn't find a Default instance for the return type: ${e.explanation}"
          )
      }

      val newMockFunction = TypeApply(
        Select(
          New(TypeIdent(mockFunctionSym)),
          mockFunctionSym.primaryConstructor
        ),
        mockFunctionTypeParams.map(ref => Inferred(ref))
      )

      // The name of the mock as passed to the MockFunctionX trait is slightly different
      // from the mock's Map keys (which is what mockName refer to). It includes a few
      // more information to make it more user friendly.
      val pos = Position.ofMacroExpansion
      // For now I'm going to assume that there can only be one type parameter list. That might
      // be (or become) wrong but it does simplify the name generation quite a bit.
      val typeParameters =
        val tpe = sym.paramSymss.filter(_.exists(_.isType)).flatten

        if tpe.isEmpty then ""
        else tpe.map(_.typeRef.show).mkString("[", ",", "]")

      val mockToStringName =
        s"<${pos.sourceFile.name}#L${pos.startLine}> $mockedClassName.${sym.name}${typeParameters}"

      val createMF =
        Apply(
          newMockFunction,
          List(ctxTerm, Literal(StringConstant(mockToStringName)))
        )
          .appliedTo(defaultTypeClass.tree)

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

  /** Find the first non-private constructor of this symbol.
    *
    * We used to use classSymbol.constructor to find the main constructor, but
    * it turns out that this can provide us with a private one, which isn't all
    * that useful to us. I also haven't found a good way to list all available
    * constructors, so I'm building that list manually by filtering on DefDef
    * and name. We then exclude the private ones and take the first constructor
    * available. If none are found, we can't build a mock class so we issue an
    * error
    *
    * @param sym
    *   the symbol we are looking a constructor at
    * @return
    *   the DefDef of the constructor, if any
    */
  private def findFirstNonPrivateConstructor(sym: Symbol): Option[DefDef] =
    sym.declarations
      .filter(_.isDefDef)
      .filter(_.name == "<init>")
      .filterNot(_.flags.is(Flags.Private))
      .map(_.tree.asInstanceOf[DefDef])
      .headOption

  private def findMethodsToOverride(sym: Symbol): List[Symbol] =
    val objectMembers = Symbol.requiredClass("java.lang.Object").methodMembers
    val anyMembers = Symbol.requiredClass("scala.Any").methodMembers

    // First we refine the methods by removing the methods inherited from Object and Any
    val candidates = sym.methodMembers
      .filter { m =>
        !(objectMembers.contains(m) || anyMembers.contains(m))
      }
      .filterNot(_.flags.is(Flags.Private)) // Do not override private members

    // Then we find the methods which have default values, and how many default values.
    // This will be used to filter out the default value methods that the compiler generate.
    // I don't know of a good way to find those via the Quotes API, so instead I use the fact
    // that the compiler always use the same naming scheme to filter them out.
    val methodsWithDefault = candidates
      .map { sym =>
        sym.name -> sym.paramSymss
          .flatMap(_.map(s => s.flags.is(Flags.HasDefault)))
          .count(identity)
      }
      .filter(_._2 > 0)

    if (methodsWithDefault.isEmpty) candidates
    else
      val names = methodsWithDefault.flatMap { case (name, count) =>
        (0 to count).map(i => s"$name$$default$$$i")
      }

      candidates.filterNot { m =>
        names.contains(m.name)
      }

  /** Walk the given symbol hierarchy to find all trait which have parameters */
  private def findParameterizedTraits(
      lookedUpSymbol: Symbol
  ): List[TypeTree] =
    val traitsWithParameters = lookedUpSymbol.typeRef.baseClasses.filter {
      sym =>
        val isTrait = sym.flags.is(Flags.Trait)
        val params = sym.primaryConstructor.paramSymss.flatten
        val notTypeParam = params.filterNot(_.isTypeParam)
        val isLookedUpClass = sym == lookedUpSymbol

        isTrait && notTypeParam.nonEmpty && !isLookedUpClass
    }

    traitsWithParameters.map(TypeTree.ref) ++ traitsWithParameters.flatMap(
      findParameterizedTraits
    )

  @experimental def generate: Expr[T & Mock] =
    val ctxTerm = ctx.asTerm match
      case i: Inlined => i.body
      case _ =>
        throw new MatchError(
          s"The MockContext expression was not inlined. Report to the author.\n ctx = $ctx"
        )

    val tType: TypeRepr = TypeRepr.of[T].dealias
    debug(s"Mocking type ${tType.show}")

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

    val objectMembers = Symbol.requiredClass("java.lang.Object").methodMembers
    val anyMembers = Symbol.requiredClass("scala.Any").methodMembers

    val methodsToOverride = findMethodsToOverride(classSymbol)

    // fields are values. TIL that scala has val without implementation :)
    val fieldsToOverride =
      classSymbol.fieldMembers.filter(_.flags.is(Flags.Deferred))

    // Start by declaring the "signature" of the class. That includes all its interfaces, but not the implementation
    val name: String = s"${classDef.name}Mock"

    val isTrait = classSymbol.flags.is(Flags.Trait)

    // When declaring a class symbol, we need the TypeTree of every parents
    // For traits we also need to extends a base class, we use java.lang.Object for that.
    val parentsTypes =
      if isTrait then
        val base = List(TypeTree.of[Object], TypeTree.of[T], TypeTree.of[Mock])

        base ++ findParameterizedTraits(classSymbol)
      else List(TypeTree.of[T], TypeTree.of[Mock])

    // When building the class definition, if T (or parameterized traits in its hierarchy)
    // has constructors that needs to be called, then we need to call those constructors.
    // For such constructors, we use `Default` implementation for terms. For types, we have
    // two ways: for T itself we use its own type as provided by the mock call site, for
    // other types we use `Any`.
    val parentsTree = parentsTypes.map { typeTree =>
      val isTypeBeingMocked = typeTree == TypeTree.of[T]

      // We only modify the TypeTree when there is a public constructor available
      findFirstNonPrivateConstructor(
        if isTypeBeingMocked then classSymbol else typeTree.symbol
      ) match
        case None => typeTree
        case Some(constructor) =>
          val allTermParams = constructor.termParamss.map(_.params).flatten

          // If there are no type or term parameters, we don't have to modify the type tree at all
          if allTermParams.isEmpty
          then typeTree
          else
            val termParams = constructor.paramss.collect {
              case TermParamClause(defs) => defs
            }
            val typeParams = constructor.paramss.collect {
              case TypeParamClause(defs) => defs
            }

            val select = New(typeTree).select(constructor.symbol)

            // special case the mocked type constructor to simplify type parameter substitution
            // For other types we cheat a bit and instead of finding the correct type parameter
            // to substitute, we simply use `Any` for all of them. Let's see if that's good a
            // thing or not. If you are debugging an issue lead by this decision, I'm sorry.
            val appliedType =
              if isTypeBeingMocked
              then select.appliedToTypes(tType.typeArgs)
              else
                typeParams.foldLeft[Term](select) {
                  case (term, typeParameters) =>
                    term.appliedToTypes(
                      typeParameters.map(_ => TypeRepr.of[Any])
                    )
                }

            termParams
              .map(_.map(valDef => defaultValueForType(valDef.tpt.tpe)))
              .foldLeft[Term](appliedType) { case (a, b) =>
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
        val tpe = cls.typeRef.memberType(m)
        val flags = (m.flags & keepThoseFlags) | Flags.Override
        val privateWithin =
          m.privateWithin.map(_.typeSymbol).getOrElse(Symbol.noSymbol)

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
      Expr.ofSeq(buildMocksSeq(methodsOverridesSymbols, ctxTerm, classDef.name))
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

            // Build the Map access. In scala that would look something like
            // MockClassName.this.mocks.asInstanceOf[MockFunctionX].apply(x)
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

      ValDef(s, Some(defaultValueForType(s.typeRef)))
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
