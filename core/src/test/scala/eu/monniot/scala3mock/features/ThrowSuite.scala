package features

import eu.monniot.scala3mock.main.withExpectations
import eu.monniot.scala3mock.functions.MockFunctions
import eu.monniot.scala3mock.main.TestExpectationEx
import eu.monniot.scala3mock.matchers.MatchAny

class ThrowSuite extends munit.FunSuite with MockFunctions {

  def * = new MatchAny

  case class TestException() extends RuntimeException
  case class AnotherTestException() extends RuntimeException

  test("should throw what it is told to (throwing)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]

      noArgFunMock.expects().throwing(new TestException)
      intercept[TestException] { noArgFunMock() }
    }
  }

  test("throw what it is told to (throws)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]

      noArgFunMock.expects().throws(new TestException)
      intercept[TestException] { noArgFunMock() }
    }
  }

  test("throw computed exception") {
    withExpectations() {
      val intFunMock = mockFunction[Int, String]

      intFunMock
        .expects(*)
        .exactly(3)
        .onCall({ (arg) =>
          if (arg == 1) throw new TestException()
          else if (arg == 2) throw new AnotherTestException()
          else "Foo"
        })

      intercept[TestException] { intFunMock(1) }
      intercept[AnotherTestException] { intFunMock(2) }
      assertEquals(intFunMock(3), "Foo")
    }
  }
}
