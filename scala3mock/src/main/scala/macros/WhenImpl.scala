package macros

import functions.{MockFunction, MockFunction0, MockFunction1}

object WhenImpl:

  import scala.quoted.*

  inline def apply[R](inline f: () => R): MockFunction0[R] =
    ${impl('f)}

  def impl[R](expr: Expr[() => R])(using Type[R], Quotes): Expr[MockFunction0[R]] =
    '{${createAccessor(expr)}.asInstanceOf[MockFunction0[R]]}

  inline def apply[T1, R](inline f: T1 => R): MockFunction1[T1, R] =
    ${impl('f)}

  def impl[T1, R](expr: Expr[T1 => R])(using Type[T1], Type[R], Quotes): Expr[MockFunction1[T1, R]] =
    '{${createAccessor(expr)}.asInstanceOf[MockFunction1[T1, R]]}

  def createAccessor(expr: Expr[Any])(using Quotes): Expr[MockFunction] =
    import quotes.reflect.*

    val (obj, name) = transcribeTree(expr.asTerm)
    val ap2 = Apply(Select.unique(obj, "accessMockFunction"), List(Literal(StringConstant(name))))

    ap2.asExprOf[MockFunction]


  def transcribeTree(using quotes: Quotes)(tree: quotes.reflect.Term): (quotes.reflect.Term, String) = {
    import quotes.reflect.*

    tree match {
      case Inlined(_, _, body) => transcribeTree(body)
      case Select(qualifier, name) =>
        //println(s"Found a select from $qualifier to $name")
        qualifier -> name
      case Block(stats, _) =>
        //println(stats.map(_.getClass))

        stats.collectFirst {
          case term: Term => transcribeTree(term)
          case t: DefDef =>
            //println(s"DefDef.rhs = ${t.rhs}")
            t.rhs.fold(throw new MatchError("DefDef without a body found. Can't find structure."))(transcribeTree)
        }.get

      case Apply(fun, _) => transcribeTree(fun)

      case TypeApply(fun, _) => transcribeTree(fun)
      case Ident(fun) => throw new MatchError(s"please declare '$fun' as MockFunctionXX or StubFunctionXX (e.g val $fun: MockFunction1[X, R] = ... if it has 1 parameter)")
      case _ => throw new MatchError(
        s"ScalaMock: Unrecognised structure: ${tree.show(using Printer.TreeStructure)}." +
          "Please open a ticket at https://github.com/fmonniot/scala3mock/issues")
    }
  }
