package mock


import main.withExpectations
import functions.MockFunctions
import main.TestExpectationEx
import matchers.MatchAny
import macros.mock

import scala.language.implicitConversions
import fixtures.TestTrait

class ThrowSuite extends munit.FunSuite with MockFunctions {

    def * = new MatchAny

    case class TestException() extends RuntimeException
    case class AnotherTestException() extends RuntimeException


    test("TODO") {
        withExpectations() {
            val m = mock[TestTrait]
        }
    }
}
