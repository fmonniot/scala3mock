package eu.monniot.scala3mock.scalatest

import context.{Mock, MockContext}
import macros.{MockImpl, WhenImpl}
import functions._
import main.TestExpectationEx
import handlers.UnorderedHandlers
import scala.collection.mutable.ListBuffer
import context.Call
import macros.Mocks
import scala.util.control.NonFatal
import org.scalatest.TestSuite
import org.scalatest.TestSuiteMixin
import org.scalatest.Outcome
import org.scalatest.Failed
import org.scalatest.exceptions.TestFailedException
import org.scalatest.exceptions.StackDepthException

trait MockFactory extends TestSuiteMixin with Mocks:
  this: TestSuite => // To prevent users from using Async with non-Async suites

  protected var autoVerify = true

  private var currentContext: MockContext = new MockContext:
    override type ExpectationException = TestFailedException

    override def newExpectationException(
        message: String,
        methodName: Option[String]
    ): ExpectationException =
      new TestFailedException(
        (_: StackDepthException) => Some(message),
        None,
        failedCodeStackDepthFn(methodName)
      )

    override def toString() = s"MockContext(callLog = $callLog)"

  given MockContext = currentContext

  private[scalatest] def failedCodeStackDepthFn(
      methodName: Option[String]
  ): StackDepthException => Int = e =>
    e.getStackTrace indexWhere { s =>
      !s.getClassName.startsWith("org.scalamock") && !s.getClassName.startsWith(
        "org.scalatest"
      ) &&
      !(s.getMethodName == "newExpectationException") && !(s.getMethodName == "reportUnexpectedCall") &&
      methodName.forall(s.getMethodName != _)
    }

  private def initializeExpectations(): Unit =
    val initialHandlers = new UnorderedHandlers

    currentContext.callLog = new ListBuffer[Call]
    currentContext.expectationContext = initialHandlers

  private def verifyExpectations(): Unit =
    currentContext.callLog foreach currentContext.expectationContext.verify _

    val oldCallLog = currentContext.callLog
    val oldExpectationContext = currentContext.expectationContext

    if !oldExpectationContext.isSatisfied then
      currentContext.reportUnsatisfiedExpectation(
        oldCallLog,
        oldExpectationContext
      )

  private def withExpectations[T](what: => T): T =
    try
      initializeExpectations()
      val result = what
      verifyExpectations()
      result
    catch
      case NonFatal(ex) =>
        // TODO clear expectations
        throw ex

  abstract override def withFixture(test: NoArgTest): Outcome =
    if (autoVerify)
      withExpectations {
        super.withFixture(test) match
          case Failed(throwable) =>
            // Throw error that caused test failure to prevent hiding it by
            // "unsatisfied expectation" exception (see issue #72)
            throw throwable
          case outcome => outcome
      }
    else
      super.withFixture(test)
