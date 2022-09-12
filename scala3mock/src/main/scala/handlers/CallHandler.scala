package handlers

import functions.{FakeFunction, FunctionAdapter0, FunctionAdapter1, FunctionAdapter3}
import context.Call
import matchers.{ArgumentMatcher, MockParameter}

class CallHandler[R]( val target: FakeFunction,  val argumentMatcher: Product => Boolean) extends Handler {
  import handlers.CallHandler._

  type Derived <: CallHandler[R]

  def repeat(range: Range) = {
    expectedCalls = range
    this.asInstanceOf[Derived]
  }

  def repeat(count: Int): CallHandler[R] = repeat(count to count)

  def never() = repeat(NEVER)
  def once() = repeat(ONCE)
  def twice() = repeat(TWICE)

  def anyNumberOfTimes() = repeat(ANY_NUMBER_OF_TIMES)
  def atLeastOnce() = repeat(AT_LEAST_ONCE)
  def atLeastTwice() = repeat(AT_LEAST_TWICE)

  def noMoreThanOnce() = repeat(NO_MORE_THAN_ONCE)
  def noMoreThanTwice() = repeat(NO_MORE_THAN_TWICE)

  def repeated(range: Range) = repeat(range)
  def repeated(count: Int) = repeat(count)
  def times() = this.asInstanceOf[Derived]

  def returns(value: R) = onCall({_ => value})
  def returning(value: R) = returns(value)

  def throws(e: Throwable) = onCall({_ => throw e})
  def throwing(e: Throwable) = throws(e)

  def onCall(handler: Product => R) = {
    onCallHandler = handler
    this.asInstanceOf[Derived]
  }

  override def toString = {
    val expected = expectedCalls match {
      case NEVER => "never"
      case ONCE => "once"
      case TWICE => "twice"
      case ANY_NUMBER_OF_TIMES => "any number of times"
      case AT_LEAST_ONCE => "at least once"
      case AT_LEAST_TWICE => "at least twice"
      case NO_MORE_THAN_ONCE => "no more than once"
      case NO_MORE_THAN_TWICE => "no more than twice"
      case r if r.size == 1 => s"${r.start} times"
      case r => s"between ${r.start} and ${r.end} times"
    }
    val actual = actualCalls match {
      case 0 => "never called"
      case 1 => "called once"
      case 2 => "called twice"
      case n => s"called $n times"
    }
    val satisfied = if (isSatisfied) "" else " - UNSATISFIED"
    s"$target$argumentMatcher $expected ($actual$satisfied)"
  }

   def handle(call: Call) = {
    if (target == call.target && !isExhausted && argumentMatcher(call.arguments)) {
      actualCalls += 1
      Some(onCallHandler(call.arguments))
    } else {
      None
    }
  }

   def verify(call: Call) = false

   def isSatisfied = expectedCalls contains actualCalls

   def isExhausted = expectedCalls.last <= actualCalls

   var expectedCalls: Range = 1 to 1
   var actualCalls: Int = 0
   var onCallHandler: Product => R = {_ => ??? /*implicitly[Defaultable[R]].default*/ }
}

object CallHandler {

   val NEVER = 0 to 0
   val ONCE = 1 to 1
   val TWICE = 2 to 2

   val ANY_NUMBER_OF_TIMES = 0 to scala.Int.MaxValue - 1
   val AT_LEAST_ONCE = 1 to scala.Int.MaxValue - 1
   val AT_LEAST_TWICE = 2 to scala.Int.MaxValue - 1

   val NO_MORE_THAN_ONCE = 0 to 1
   val NO_MORE_THAN_TWICE = 0 to 2
}

trait Verify { self: CallHandler[_] =>

   override def handle(call: Call) = sys.error("verify should appear after all code under test has been exercised")

   override def verify(call: Call) = {
    if (self.target == call.target && argumentMatcher(call.arguments)) {
      actualCalls += 1
      true
    } else {
      false
    }
  }
}

