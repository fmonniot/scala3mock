package eu.monniot.scala3mock.cats

import cats.MonadError
import eu.monniot.scala3mock.context.{Call, MockContext}
import eu.monniot.scala3mock.functions.MockFunction1
import eu.monniot.scala3mock.handlers.{CallHandler, Handler, UnorderedHandlers}
import eu.monniot.scala3mock.main.TestExpectationEx

import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal


// A standalone function to run a test with a mock context, asserting all expectations at the end.
def withExpectations[F[_], A](verifyAfterRun: Boolean = true)(
    f: MockContext ?=> F[A]
)(using MonadError[F, Throwable]): F[A] =

  val ctx = new MockContext:
    override type ExpectationException = TestExpectationEx

    override def newExpectationException(
        message: String,
        methodName: Option[String]
    ): ExpectationException =
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
    MonadError[F, Throwable] // TODO Wire the result and verification correctly
    val result = f(using ctx)
    if verifyAfterRun then verifyExpectations()
    result
  catch
    case NonFatal(ex) =>
      // do not verify expectations - just clear them. Throw original exception
      // see issue #72
      throw ex
