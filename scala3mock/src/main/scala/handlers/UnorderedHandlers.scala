package handlers

import context.Call
import scala.util.control.NonLocalReturns.*

class UnorderedHandlers(logging: Boolean = false) extends Handlers {

  def handle(call: Call): Option[Any] = this.synchronized {
    if (logging) println(s"handling unordered call $call")

    // TODO Rewrite
    returning {
      for (handler <- handlers) {
        val r = handler.handle(call)
        if (r.isDefined)
          throwReturn(r)
      }
      None
    }
  }

  def verify(call: Call): Boolean = this.synchronized {
    if (logging) println(s"verifying unordered call $call")
    
    // TODO rewrite
    returning {
      for (handler <- handlers) {
        if (handler.verify(call))
          throwReturn(true)
      }
      false
    }
  }

  protected val prefix = "inAnyOrder"
}
