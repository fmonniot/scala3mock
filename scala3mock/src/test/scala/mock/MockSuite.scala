package mock


import main.withExpectations
import functions.MockFunctions
import main.TestExpectationEx
import matchers.MatchAny
import macros.{mock, when}

import scala.language.implicitConversions
import fixtures.*
import macros.PrintAst

class MockSuite extends munit.FunSuite with MockFunctions {

    def * = new MatchAny

    case class TestException() extends RuntimeException
    case class AnotherTestException() extends RuntimeException

    


    test("TODO") {
        withExpectations() {
            val m = mock[TestTrait]            
            // TODO Missing MockFunction impl for 3+ parameters
            //val a = mock[ManyParamsClass]
            //val b = mock[ManyParamsTrait]

            // TODO Missing replacing the class type parameters in the impl
            //val c = mock[PolymorphicClass[String]]
            //val d = mock[PolymorphicTrait[Int]]

            // Seems to be special case for scala.js ? Not entirely sure though
            // I suppose that mean we can skip those for now.
            //val e = mock[SpecializedClass[Int]]
            //val f = mock[SpecializedClass2[Int, Float]]

            // TODO Needs to ignore private methods
            //val g = mock[TestClass]


            /*
            when(m.polycurried(_: String)(_: Int))
                .expects("a", 2)
                .returns("c" -> 6)

            assertEquals(m.polycurried("a")(2), "c" -> 6)
            */

            /*
            when(m.polymorphic _)
                .expects(List(2))
                .returns("a1")

            assertEquals(m.polymorphic(List(2)), "a1")

            when(m.upperBound _)
                .expects(TestException())
                .returns(1)

            assertEquals(m.upperBound(TestException()), 1)
            */

            /*
            when(m.overloaded _)
                .expects("wild")
                .returns("a0")

            assertEquals(m.overloaded("wild"), "a0")
            */

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
