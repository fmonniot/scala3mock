package features

import eu.monniot.scala3mock.ScalaMocks
import eu.monniot.scala3mock.MockExpectationFailed

class CallCountSuite extends munit.FunSuite with ScalaMocks {

  test("should fail if an unexpected call is made") {
    withExpectations() {
      val intFunMock = mockFunction[Int, String]

      intercept[MockExpectationFailed] { intFunMock(42) }
    }
  }

  test("should fail if a method isn't called often enough (once)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(42).once
      intFunMock(42)
    }
  }

  test("should not fail if a method is called once (once)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(42).once
      intFunMock(42)
    }
  }

  test("should fail if a method is called too often (once)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(42).twice

      intFunMock(42)
      intFunMock(42)
      intercept[MockExpectationFailed] { intFunMock(42) }
    }
  }

  test("should fail if a method isn't called often enough (twice)") {
    intercept[MockExpectationFailed] {
      withExpectations() {
        val noArgFunMock = mockFunction[String]
        val intFunMock = mockFunction[Int, String]

        intFunMock.expects(42).twice
        intFunMock(42)
      }
    }
  }

  test("should fail if a method is called too often (twice)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(42).twice

      intFunMock(42)
      intFunMock(42)
      intercept[MockExpectationFailed] { intFunMock(42) }
    }
  }

  test("should handle noMoreThanTwice call count (zero)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).noMoreThanTwice
    }
  }

  test("should handle noMoreThanTwice call count (one)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).noMoreThanTwice
      intFunMock(2)
    }
  }

  test("should handle noMoreThanTwice call count (two)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).noMoreThanTwice
      intFunMock(2)
      intFunMock(2)
    }
  }

  test("should handle noMoreThanTwice call count (three)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).noMoreThanTwice
      intFunMock(2)
      intFunMock(2)
      intercept[MockExpectationFailed] { intFunMock(42) }
    }
  }

  test("should handle never call count (zero)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).never
    }
  }

  test("should handle never call count (one)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).never
      intercept[MockExpectationFailed] { intFunMock(2) }
    }
  }

  test("should handle exactly(3) call count (3)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).exactly(3)

      intFunMock(2)
      intFunMock(2)
      intFunMock(2)
    }
  }

  test("should handle repeated(1 to 2) call count (0)") {
    intercept[MockExpectationFailed] {
      withExpectations() {
        val intFunMock = mockFunction[Int, String]

        intFunMock.expects(2).repeated(1 to 2)
      }
    }
  }

  test("should handle repeated(1 to 2) call count (1)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).repeated(1 to 2)
      intFunMock(2)
    }
  }

  test("should handle repeat(1 to 2) call count (2)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).repeated(1 to 2)
      intFunMock(2)
      intFunMock(2)
    }
  }

  test("should handle repeated(1 to 2) call count (3)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).repeated(1 to 2)
      intFunMock(2)
      intFunMock(2)
      intercept[MockExpectationFailed] { intFunMock(2) }
    }
  }

  test("should handle exactly(2) call count (2)") {
    withExpectations() {
      val noArgFunMock = mockFunction[String]
      val intFunMock = mockFunction[Int, String]

      intFunMock.expects(2).exactly(2)
      intFunMock(2)
      intFunMock(2)
    }
  }
}
