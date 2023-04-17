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

trait MockFactory extends TestSuiteMixin with BaseFactory:
  this: TestSuite => // To prevent users from using Async with non-Async suites

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
