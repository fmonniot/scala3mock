package mock


import main.withExpectations
import functions.MockFunctions
import main.TestExpectationEx
import matchers.MatchAny
import macros.{mock, when}

import scala.language.implicitConversions
import fixtures.TestTrait

class MockSuite extends munit.FunSuite with MockFunctions {

    def * = new MatchAny

    case class TestException() extends RuntimeException
    case class AnotherTestException() extends RuntimeException


    test("TODO") {
        withExpectations() {
            val m = mock[TestTrait]

            when(m.repeatedParam _)
                .expects(0, Seq.empty)
                .returning("hello")

            assertEquals(m.repeatedParam(0), "hello")
        }
    }
}
