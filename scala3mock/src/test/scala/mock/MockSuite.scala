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

            /*
            when(m.repeatedParam _)
                .expects(0, Seq.empty)
                .returning("hello")

            assertEquals(m.repeatedParam(0), "hello")
            */

            /*
            when(() => m.nullary)
                .expects()
                .returning("a")

            when(() => m.noParams())
                .expects()
                .returning("a2")

            assertEquals(m.nullary, "a")
            assertEquals(m.noParams(), "a2")
            */

            /*
            when(m.overloaded(_: Int))
                .expects(1)
                .returning("b")
            
            when(m.overloaded(_: String))
                .expects("a")
                .returning("c")

            when(m.overloaded(_: Int, _: Double))
                .expects(3, 4.2)
                .returning("d")

            assertEquals(m.overloaded(1), "b")
            assertEquals(m.overloaded("a"), "c")
            assertEquals(m.overloaded(3, 4.2), "d")
            */
        }
    }
}
