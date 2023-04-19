package eu.monniot.scala3mock.context

import eu.monniot.scala3mock.functions.FakeFunction

case class Call(target: FakeFunction, arguments: Product):
  override def toString: String =
    s"$target${arguments.productIterator.mkString("(", ", ", ")")}"
