package eu.monniot.scala3mock.macros

import eu.monniot.scala3mock.functions.*
import eu.monniot.scala3mock.context.Mock
import eu.monniot.scala3mock.main.Default

private[scala3mock] object WhenImpl:

  import scala.quoted.*

  // We disable formatting for the apply & impl functions so that we can save line height.
  // Without disable, appy12 would take around 36 lines vs the current one. impl is even worse.
  // format: off

  def apply[R](f: Expr[() => R])(using Type[R], Quotes): Expr[MockFunction0[R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction0[R]]}

  def apply[T1, R](f: Expr[T1 => R])(using Type[T1], Type[R], Quotes): Expr[MockFunction1[T1, R]] =
    '{
      val mf = ${createMockFunction(f)}.asInstanceOf[MockFunction1[T1, R]]

      println(s"selected MockFunction1 = $mf")
      println(s"T1 = " + ${Expr(Type.show[T1])})
      println(s"R =  " + ${Expr(Type.show[R])})

      mf
    }

  def apply[T1, T2, R](f: Expr[(T1, T2) => R])(using Type[T1], Type[T2], Type[R], Quotes): Expr[MockFunction2[T1, T2, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction2[T1, T2, R]]}

  def apply[T1, T2, T3, R](f: Expr[(T1, T2, T3) => R])(using Type[T1], Type[T2], Type[T3], Type[R], Quotes): Expr[MockFunction3[T1, T2, T3, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction3[T1, T2, T3, R]]}

  def apply[T1, T2, T3, T4, R](f: Expr[(T1, T2, T3, T4) => R])(using Type[T1], Type[T2], Type[T3], Type[T4], Type[R], Quotes): Expr[MockFunction4[T1, T2, T3, T4, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction4[T1, T2, T3, T4, R]]}

  def apply[T1, T2, T3, T4, T5, R](f: Expr[(T1, T2, T3, T4, T5) => R])(using 
      Type[T1], Type[T2], Type[T3], Type[T4], Type[T5],
      Type[R], Quotes): Expr[MockFunction5[T1, T2, T3, T4, T5, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction5[T1, T2, T3, T4, T5, R]]}

  def apply[T1, T2, T3, T4, T5, T6, R](f: Expr[(T1, T2, T3, T4, T5, T6) => R])(using 
      Type[T1], Type[T2], Type[T3], Type[T4], Type[T5], Type[T6],
      Type[R], Quotes): Expr[MockFunction6[T1, T2, T3, T4, T5, T6, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction6[T1, T2, T3, T4, T5, T6, R]]}

  def apply[T1, T2, T3, T4, T5, T6, T7, R](f: Expr[(T1, T2, T3, T4, T5, T6, T7) => R])(using 
      Type[T1], Type[T2], Type[T3], Type[T4], Type[T5], Type[T6], Type[T7],
      Type[R], Quotes): Expr[MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction7[T1, T2, T3, T4, T5, T6, T7, R]]}

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, R](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8) => R])(using 
      Type[T1], Type[T2], Type[T3], Type[T4], Type[T5], Type[T6], Type[T7], Type[T8],
      Type[R], Quotes): Expr[MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R]]}

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9) => R])(using 
      Type[T1], Type[T2], Type[T3], Type[T4], Type[T5], Type[T6], Type[T7], Type[T8],Type[T9],
      Type[R], Quotes): Expr[MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]]}
  
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](f: Expr[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R])(using 
      Type[T1], Type[T2], Type[T3], Type[T4], Type[T5], Type[T6], Type[T7], Type[T8],Type[T9],Type[T10],
      Type[R], Quotes): Expr[MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]]}

  // format: on

  def createMockFunction(expr: Expr[Any])(using Quotes): Expr[MockFunction] =
    import quotes.reflect.*

    val (obj, name) = transcribeTree(expr.asTerm)

    '{
      if ${ obj.asExprOf[Any] }.isInstanceOf[Mock]
      then
        ${ obj.asExprOf[Any] }
          .asInstanceOf[Mock]
          .accessMockFunction(${ Expr(name) })
      else throw Mock.ReceiverIsNotAMock
    }

  def transcribeTree(using quotes: Quotes)(
      tree: quotes.reflect.Term
  ): (quotes.reflect.Term, String) =
    import quotes.reflect.*

    tree match
      case Inlined(_, _, body)         => transcribeTree(body)
      case s @ Select(qualifier, name) =>
        // We don't simply use the qualTpe.classSymbol accessor because we might
        // have multiple known type mixed in. At the moment we only support excluding
        // the Mock trait from one other base class. The filtering here needs to change
        // if (or when) the when macro support union, intersection or with mixin.
        //
        // dev note: TLDR the filtering is only useful when using `MockImpl.apply` or
        // `MockImpl.debug` because those returns `T & Mock`.
        val objectSym = Symbol.requiredClass("java.lang.Object")
        val anySym = Symbol.requiredClass("scala.Any")
        val matchableSym = Symbol.requiredClass("scala.Matchable")
        val mockSym = Symbol.requiredClass("context.Mock")
        val classSymbol = qualifier.tpe.baseClasses
          .filterNot(sym =>
            sym == objectSym || sym == anySym || sym == matchableSym || sym == mockSym
          )
          .headOption

        val n = s.signature match
          case None =>
            // No signature means a nullary function. The mock name will always be the
            // same as the function name (no overload possible with nullary)
            name

          case Some(signature) =>
            classSymbol.map(_.methodMembers) match
              case None =>
                report.errorAndAbort(
                  "The when parameter is composed of more than one type, which isn't supported at the moment."
                )

              case Some(methods) =>
                val overload =
                  utils.sortSymbolsViaSignature(methods.filter(_.name == name))

                // If there are no overload, let's use the method name as the mock key. Otherwise
                // append the index of the overload. Using the same sort here and in the mock
                // declaration is important for the indices to match.
                if overload.length == 1 then name
                else {
                  val idx = overload.indexWhere(_.signature == signature)

                  s"${name}-$idx"
                }

        qualifier -> n

      case Block(stats, _) =>
        // println(stats.map(_.getClass))

        stats.collectFirst {
          case term: Term => transcribeTree(term)
          case t: DefDef  =>
            // println(s"DefDef.rhs = ${t.rhs}")

            t.rhs match
              case None =>
                report.errorAndAbort(
                  "Method definition doesn't have a body to investigate. Please report your use case to the maintainers."
                )
              case Some(term) => transcribeTree(term)
        }.get

      case Apply(fun, _) => transcribeTree(fun)

      case TypeApply(fun, _) => transcribeTree(fun)
      case Ident(fun)        =>
        // TODO That's probably not a valid advice anymore. Write a test case and update the error message accordingly.
        report.errorAndAbort(
          s"please declare '$fun' as MockFunctionXX or StubFunctionXX (e.g val $fun: MockFunction1[X, R] = ... if it has 1 parameter)"
        )
      case _ =>
        report.errorAndAbort(
          s"ScalaMock: Unrecognised structure: ${tree.show(using Printer.TreeStructure)}." +
            "Please open a ticket at https://github.com/fmonniot/scala3mock/issues"
        )
