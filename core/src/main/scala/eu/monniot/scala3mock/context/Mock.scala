package eu.monniot.scala3mock.context

import eu.monniot.scala3mock.functions.MockFunction

import scala.annotation.unused

private[scala3mock] trait Mock:

  // This is a hack to go around my inability to call methods defined on companion objects
  @unused
  protected def createMap(
      elems: (String, MockFunction)*
  ): Map[String, MockFunction] = Map.from(elems)

  def accessMockFunction(name: String): MockFunction

object Mock:
  case object ReceiverIsNotAMock
      extends Throwable(
        "The object being mocked is not mockable. Use the mock[] function to create a mock."
      )
