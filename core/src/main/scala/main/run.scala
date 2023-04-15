package main

import context.{Call, MockContext}
import functions.MockFunction1
import handlers.{CallHandler, Handler, UnorderedHandlers}

import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal


class TestExpectationEx(message: String, methodName: Option[String]) extends Throwable:
  override def getMessage(): String = message

// A standalone function to run a test with a mock context, asserting all expectations at the end.
def withExpectations[A](verifyAfterRun: Boolean = true)(f: MockContext ?=> A): A =

  val ctx = new MockContext:
    override type ExpectationException = TestExpectationEx

    override def newExpectationException(message: String, methodName: Option[String]): ExpectationException =
      new TestExpectationEx(message, methodName)

    override def toString() = s"MockContext(callLog = $callLog)"

  def initializeExpectations(): Unit =
    val initialHandlers = new UnorderedHandlers

    ctx.callLog = new ListBuffer[Call]
    ctx.expectationContext = initialHandlers

  def verifyExpectations(): Unit =
    ctx.callLog foreach ctx.expectationContext.verify _

    val oldCallLog = ctx.callLog
    val oldExpectationContext = ctx.expectationContext

    if !oldExpectationContext.isSatisfied then
      ctx.reportUnsatisfiedExpectation(oldCallLog, oldExpectationContext)

  try
    initializeExpectations()
    val result = f(using ctx)
    if verifyAfterRun then verifyExpectations()
    result
  catch
    case NonFatal(ex) =>
      // do not verify expectations - just clear them. Throw original exception
      // see issue #72
      throw ex










