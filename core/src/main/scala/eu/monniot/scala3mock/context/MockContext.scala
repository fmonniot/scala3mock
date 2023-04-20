package eu.monniot.scala3mock.context

import eu.monniot.scala3mock.handlers.{CallHandler, Handlers, UnorderedHandlers}

import scala.collection.mutable.ListBuffer

trait MockContext:
  type ExpectationException <: Throwable
  def newExpectationException(
      message: String,
      methodName: Option[String] = None
  ): ExpectationException

  private[scala3mock] var callLog: ListBuffer[Call] = ListBuffer()
  private[scala3mock] var expectationContext: Handlers = UnorderedHandlers()
  // val mockNameGenerator: MockNameGenerator = new MockNameGenerator()

  def add[E <: CallHandler[_]](e: E): E =
    assert(
      expectationContext != null,
      "Null expectation context - missing withExpectations?"
    )
    expectationContext.add(e)
    e

  def reportUnexpectedCall(call: Call) =
    throw newExpectationException(
      s"Unexpected call: $call\n\n${errorContext(callLog, expectationContext)}",
      Some(call.target.name)
    )

  def reportUnsatisfiedExpectation(
      callLog: ListBuffer[Call],
      expectationContext: Handlers
  ) =
    throw newExpectationException(
      s"Unsatisfied expectation:\n\n${errorContext(callLog, expectationContext)}"
    )

  def errorContext(callLog: ListBuffer[Call], expectationContext: Handlers) =
    s"Expected:\n$expectationContext\n\nActual:\n$callLog"
