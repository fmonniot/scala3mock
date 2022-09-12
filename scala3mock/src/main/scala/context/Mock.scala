package context

import functions.MockFunction

import scala.annotation.unused

trait Mock {

  // This is a hack to go around my inability to call methods defined on companion objects
  @unused
  protected def createMap(elems: (String, MockFunction)*): Map[String, MockFunction] = Map.from(elems)

  def accessMockFunction(name: String): MockFunction
}
