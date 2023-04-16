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
import handlers.Handlers

trait MockFactory extends TestSuiteMixin with Mocks:
  this: TestSuite => // To prevent users from using Async with non-Async suites

  protected var autoVerify = true


  private val suiteCallLog: ListBuffer[Call] = new ListBuffer[Call]
  private val suiteExpectationContext: Handlers = new UnorderedHandlers

  // We should have two expectationContext & callLog:
  // - One at the suite level
  // - A second at the test level
  // The second one should inherit from the first as well
  given currentContext: MockContext = new MockContext:
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

    // Initialize the current context with the suite-level calls and expectations
    callLog = suiteCallLog
    expectationContext = suiteExpectationContext

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
    currentContext.callLog = new ListBuffer[Call]
    currentContext.expectationContext = new UnorderedHandlers

    // Import the suite calls & expectations into the test one
    currentContext.callLog.addAll(suiteCallLog)
    suiteExpectationContext.list.foreach(currentContext.expectationContext.add)

  private def verifyExpectations(): Unit =
    currentContext.callLog.foreach(currentContext.expectationContext.verify)

    // We need to call this before clearing test expectations. Otherwise the call
    // and expectations made during suite initialization will be cleared before
    // checking if they have been used. Note that it's an issue because Handler
    // are mutable and suite/current expectation context share the same references.
    val isSatisfied = currentContext.expectationContext.isSatisfied

    val oldCallLog = currentContext.callLog
    val oldExpectationContext = currentContext.expectationContext

    clearTestExpectations()

    if !isSatisfied then
      currentContext.reportUnsatisfiedExpectation(
        oldCallLog,
        oldExpectationContext
      )

  
  private def clearTestExpectations(): Unit =
    // to forbid setting expectations after verification is done 
    currentContext.callLog = suiteCallLog
    currentContext.expectationContext = suiteExpectationContext
    suiteExpectationContext.list.foreach(_.reset())


  private def withExpectations[T](name: String)(what: => T): T =
    try
      initializeExpectations()
      val result = what
      verifyExpectations()
      result
    catch
      case NonFatal(ex) =>
        clearTestExpectations()
        throw ex

  abstract override def withFixture(test: NoArgTest): Outcome =
    if (autoVerify)
      withExpectations(test.name) {
        super.withFixture(test) match
          case Failed(throwable) =>
            // Throw error that caused test failure to prevent hiding it by
            // "unsatisfied expectation" exception (see issue #72)
            throw throwable
          case outcome => outcome
      }
    else
      super.withFixture(test)
