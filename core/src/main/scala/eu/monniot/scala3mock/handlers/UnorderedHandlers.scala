package eu.monniot.scala3mock.handlers

import eu.monniot.scala3mock.context.Call
import scala.util.control.NonLocalReturns.*

class UnorderedHandlers(logging: Boolean = false) extends Handlers:

  def handle(call: Call): Option[Any] = this.synchronized {
    if logging then println(s"handling unordered call $call")

    findFirstOpt(handlers, _.handle(call))
  }

  def verify(call: Call): Boolean = this.synchronized {
    if logging then println(s"verifying unordered call $call")

    handlers.find(_.verify(call)).isDefined
  }

  def reset(): Unit = handlers.foreach(_.reset())

  protected val prefix = "inAnyOrder"

  /** Finds the first element of `iter` satisfying a predicate, if any.
    *
    * @param iter
    *   the collection of elements
    * @param p
    *   the predicate used to test elements.
    * @return
    *   an option value containing the first element in the $coll that satisfies
    *   `p.isDefined`, or `None` if none exists.
    */
  private def findFirstOpt[A, B](
      iter: IterableOnce[A],
      p: A => Option[B]
  ): Option[B] = {
    val it = iter.iterator
    while (it.hasNext) {
      val a = it.next()
      val mayB = p(a)
      if (mayB.isDefined) return mayB
    }
    None
  }
