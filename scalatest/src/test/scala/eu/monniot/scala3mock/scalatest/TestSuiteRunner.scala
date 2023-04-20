package eu.monniot.scala3mock.scalatest

import org.scalatest._
import org.scalatest.events.{Event, TestFailed}
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps
import scala.reflect.ClassTag

trait TestSuiteRunner { this: Matchers =>

  /** Executes single ScalaTest test case and returns its outcome (i.e. either
    * TestSucccess or TestFailure)
    */
  def runTestCase[T <: Suite](suite: T): Event = {
    class TestReporter extends Reporter {
      var lastEvent: Option[Event] = None
      override def apply(e: Event): Unit = { lastEvent = Some(e) }
    }

    val reporter = new TestReporter
    suite.run(None, Args(reporter))
    reporter.lastEvent.get
  }

  def getThrowable[ExnT <: Throwable: ClassTag](event: Event): ExnT = {
    event shouldBe a[TestFailed]

    val testCaseError = event.asInstanceOf[TestFailed].throwable.get
    testCaseError shouldBe a[ExnT]
    testCaseError.asInstanceOf[ExnT]
  }

  def getErrorMessage[ExnT <: Throwable: ClassTag](event: Event): String = {
    getThrowable[ExnT](event).getMessage()
  }
}
