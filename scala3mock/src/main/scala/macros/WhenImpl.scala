package macros

import functions.*
import context.Mock

private[macros] object WhenImpl:

  import scala.quoted.*

  inline def apply[R](inline f: () => R): MockFunction0[R] =
    ${impl('f)}

  inline def apply[T1, R](inline f: T1 => R): MockFunction1[T1, R] =
    ${impl('f)}

  inline def apply[T1, T2, R](inline f: (T1, T2) => R): MockFunction2[T1, T2, R] =
    ${impl('f)}


  def impl[R](f: Expr[() => R])(using Type[R], Quotes): Expr[MockFunction0[R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction0[R]]}

  def impl[T1, R](f: Expr[T1 => R])(using Type[T1], Type[R], Quotes): Expr[MockFunction1[T1, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction1[T1, R]]}

  def impl[T1, T2, R](f: Expr[(T1, T2) => R])(using Type[T1], Type[T2], Type[R], Quotes): Expr[MockFunction2[T1, T2, R]] =
    '{${createMockFunction(f)}.asInstanceOf[MockFunction2[T1, T2, R]]}


  def createMockFunction(expr: Expr[Any])(using Quotes): Expr[MockFunction] =
    import quotes.reflect.*

    val (obj, name) = transcribeTree(expr.asTerm)

    '{ 
      if ${obj.asExprOf[Any]}.isInstanceOf[Mock]
      then ${obj.asExprOf[Any]}.asInstanceOf[Mock].accessMockFunction(${Expr(name)})
      else throw Mock.ReceiverIsNotAMock
    }


  def transcribeTree(using quotes: Quotes)(tree: quotes.reflect.Term): (quotes.reflect.Term, String) =
    import quotes.reflect.*

    tree match
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
