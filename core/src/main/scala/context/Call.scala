package context

import functions.FakeFunction

case class Call(target: FakeFunction, arguments: Product):
  override def toString: String =
    s"$target${arguments.productIterator.mkString("(", ", ", ")")}"
