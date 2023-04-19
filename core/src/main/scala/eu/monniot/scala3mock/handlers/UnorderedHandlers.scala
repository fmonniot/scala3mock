package eu.monniot.scala3mock.handlers

import eu.monniot.scala3mock.context.Call
import scala.util.control.NonLocalReturns.*

class UnorderedHandlers(logging: Boolean = false) extends Handlers:

  def handle(call: Call): Option[Any] = this.synchronized {
    if logging then println(s"handling unordered call $call")

    // TODO Rewrite
    returning {
      for handler <- handlers do
        val r = handler.handle(call)
        if r.isDefined then
          throwReturn(r)
      None
    }
  }

  def verify(call: Call): Boolean = this.synchronized {
    if logging then println(s"verifying unordered call $call")
    
    // TODO rewrite
    returning {
      for handler <- handlers do
        if handler.verify(call) then
          throwReturn(true)
      false
    }
  }

  def reset(): Unit = handlers.foreach(_.reset())

  protected val prefix = "inAnyOrder"
