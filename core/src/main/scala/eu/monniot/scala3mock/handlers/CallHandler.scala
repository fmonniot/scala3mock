package eu.monniot.scala3mock.handlers

import eu.monniot.scala3mock.functions.{
  FakeFunction,
  FunctionAdapter0,
  FunctionAdapter1,
  FunctionAdapter3
}
import eu.monniot.scala3mock.context.Call
import eu.monniot.scala3mock.matchers.ArgumentMatcher
import eu.monniot.scala3mock.main.Default

class CallHandler[R: Default](
    val target: FakeFunction,
    val argumentMatcher: Product => Boolean
) extends Handler:
  import eu.monniot.scala3mock.handlers.CallHandler._

  type Derived <: CallHandler[R]

  def repeated(range: Range): Derived =
    expectedCalls = range
    this.asInstanceOf[Derived]

  def repeated(atLeast: Int = 1, atMost: Int = Int.MaxValue - 1): Derived =
    repeated(atLeast to atMost)

  def exactly(count: Int): Derived = repeated(count to count)

  def never = repeated(NEVER)
  def once = repeated(ONCE)
  def twice = repeated(TWICE)

  def anyNumberOfTimes = repeated(ANY_NUMBER_OF_TIMES)
  def atLeastOnce = repeated(AT_LEAST_ONCE)
  def atLeastTwice = repeated(AT_LEAST_TWICE)

  def noMoreThanOnce = repeated(NO_MORE_THAN_ONCE)
  def noMoreThanTwice = repeated(NO_MORE_THAN_TWICE)

  def returns(value: R) = onCall({ _ => value })
  def returning(value: R) = returns(value)

  def throws(e: Throwable) = onCall({ _ => throw e })
  def throwing(e: Throwable) = throws(e)

  def onCall(handler: Product => R) =
    onCallHandler = handler
    this.asInstanceOf[Derived]

  override def toString =
    val expected = expectedCalls match
      case NEVER               => "never"
      case ONCE                => "once"
      case TWICE               => "twice"
      case ANY_NUMBER_OF_TIMES => "any number of times"
      case AT_LEAST_ONCE       => "at least once"
      case AT_LEAST_TWICE      => "at least twice"
      case NO_MORE_THAN_ONCE   => "no more than once"
      case NO_MORE_THAN_TWICE  => "no more than twice"
      case r if r.size == 1    => s"${r.start} times"
      case r                   => s"between ${r.start} and ${r.end} times"
    val actual = actualCalls match
      case 0 => "never called"
      case 1 => "called once"
      case 2 => "called twice"
      case n => s"called $n times"
    val satisfied = if isSatisfied then "" else " - UNSATISFIED"
    s"$target$argumentMatcher $expected ($actual$satisfied)"

  def handle(call: Call) =
    if target == call.target && !isExhausted && argumentMatcher(call.arguments)
    then
      actualCalls += 1
      Some(onCallHandler(call.arguments))
    else None

  def verify(call: Call) = false

  def isSatisfied = expectedCalls contains actualCalls

  def isExhausted = expectedCalls.last <= actualCalls

  def reset(): Unit = actualCalls = 0

  var expectedCalls: Range = 1 to 1
  var actualCalls: Int = 0
  var onCallHandler: Product => R = { _ => Default[R].default }

object CallHandler:

  val NEVER = 0 to 0
  val ONCE = 1 to 1
  val TWICE = 2 to 2

  val ANY_NUMBER_OF_TIMES = 0 to scala.Int.MaxValue - 1
  val AT_LEAST_ONCE = 1 to scala.Int.MaxValue - 1
  val AT_LEAST_TWICE = 2 to scala.Int.MaxValue - 1

  val NO_MORE_THAN_ONCE = 0 to 1
  val NO_MORE_THAN_TWICE = 0 to 2

trait Verify { self: CallHandler[_] =>

  override def handle(call: Call) = sys.error(
    "verify should appear after all code under test has been exercised"
  )

  override def verify(call: Call) =
    if self.target == call.target && argumentMatcher(call.arguments) then
      actualCalls += 1
      true
    else false
}
