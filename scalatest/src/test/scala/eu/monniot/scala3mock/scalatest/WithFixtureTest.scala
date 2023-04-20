package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.MockFactory
import eu.monniot.scala3mock.mockable.TestTrait

import org.scalatest._
import org.scalatest.events.TestSucceeded
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class WithFixtureTest extends AnyFlatSpec with Matchers with TestSuiteRunner {

  object EventLogger {
    var events: List[String] = List.empty
    def logEvent(event: String) = { events = events :+ event }
  }

  trait SuiteWrapper extends SuiteMixin with TestSuite {
    abstract override def withFixture(test: NoArgTest): Outcome = {
      EventLogger.logEvent("SuiteWrapper setup")
      val outcome = super.withFixture(test)
      EventLogger.logEvent("SuiteWrapper cleanup")
      outcome
    }
  }

  class TestedSuite
      extends AnyFunSuite
      with SuiteWrapper
      with MockFactory
      with Matchers {
    test("execute block of code") {
      val mockedTrait = mock[TestTrait]
      when(mockedTrait.oneParamMethod).expects(1).onCall { (arg: Int) =>
        EventLogger.logEvent("mock method called")
        "one"
      }

      mockedTrait.oneParamMethod(1) shouldBe "one"
    }
  }

  it can "be mixed together with other traits which override withFixture" in {
    val outcome = runTestCase[TestedSuite](new TestedSuite)
    outcome shouldBe a[TestSucceeded]
    EventLogger.events shouldBe List(
      "SuiteWrapper setup",
      "mock method called",
      "SuiteWrapper cleanup"
    )
  }

}
