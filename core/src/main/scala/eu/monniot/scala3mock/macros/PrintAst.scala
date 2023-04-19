package eu.monniot.scala3mock.macros

object PrintAst:

  import scala.quoted.*

  inline def apply[T](inline stuff: T) =
    ${inspectExpr('stuff)}

  inline def apply[T] =
    ${inspectType[T]}

  private def inspectExpr[T](x: Expr[T])(using Quotes, Type[T]): Expr[Any] =
    import quotes.reflect.*

    val term = x.asTerm
    println(s"===========Expression of type ${Type.show[T]}=========:")
    println()
    println(term.show(using Printer.TreeAnsiCode))
    println()
    println(term.show(using Printer.TreeStructure))
    println()
    println("===========================")

    x

  private def inspectType[T](using Type[T], Quotes): Expr[Any] =
    import quotes.reflect.*

    val typeRepr: TypeRepr = TypeRepr.of[T].dealias
    println(s"===========Type ${Type.show[T]}=========:")
    println()
    println(s"typeSymbol=${typeRepr.typeSymbol}; ")
    println()
    println(typeRepr.typeSymbol.tree.show(using Printer.TreeAnsiCode))
    println()
    println(typeRepr.typeSymbol.tree.show(using Printer.TreeStructure))
    println()
    println("===========================")

    '{ () }

