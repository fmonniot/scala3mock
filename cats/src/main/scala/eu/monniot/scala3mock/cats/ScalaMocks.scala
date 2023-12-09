package eu.monniot.scala3mock.cats

import cats.MonadError
import eu.monniot.scala3mock.context.{Call, MockContext}
import eu.monniot.scala3mock.functions.MockFunction1
import eu.monniot.scala3mock.handlers.{CallHandler, Handler, UnorderedHandlers}
import eu.monniot.scala3mock.MockExpectationFailed

import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

object ScalaMocks extends ScalaMocks

/** Helper trait that provide access to all components (mandatory or optional)
  * used by the library to build mocks.
  */
trait ScalaMocks
    extends eu.monniot.scala3mock.functions.MockFunctions
    with eu.monniot.scala3mock.macros.Mocks
    with eu.monniot.scala3mock.matchers.Matchers:

  // apparently using export in 3.2.2 lose the default value of the
  // parameter. That might have been fixed in 3.3+, but we can't use
  // that version so for now we will duplicate the definition.
  def withExpectations[F[_], A](verifyAfterRun: Boolean = true)(
      f: MockContext ?=> F[A]
  )(using MonadError[F, Throwable]): F[A] =
    eu.monniot.scala3mock.cats.withExpectations(verifyAfterRun)(f)

// A standalone function to run a test with a mock context, asserting all expectations at the end.
def withExpectations[F[_], A](verifyAfterRun: Boolean = true)(
    f: MockContext ?=> F[A]
)(using MonadError[F, Throwable]): F[A] =

  val ctx = new MockContext:
    override type ExpectationException = MockExpectationFailed

    override def newExpectationException(
        message: String,
        methodName: Option[String]
    ): ExpectationException =
      new MockExpectationFailed(message, methodName)

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

  initializeExpectations()
  val me = MonadError[F, Throwable]

  me.flatMap(f(using ctx)) { a =>
    if verifyAfterRun then
      try
        verifyExpectations()
        me.pure(a)
      catch case t => me.raiseError(t)
    else me.pure(a)
  }
