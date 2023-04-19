package features

import eu.monniot.scala3mock.main.withExpectations
import eu.monniot.scala3mock.functions.MockFunctions
import eu.monniot.scala3mock.main.TestExpectationEx
import eu.monniot.scala3mock.matchers.MatchAny

import scala.language.implicitConversions

class ReturnSuite extends munit.FunSuite with MockFunctions {

    def * = new MatchAny

    test("should return null by default") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]
            val intToIntMock = mockFunction[Int, Int]

            intToStringMock.expects(*)
            assertEquals(intToStringMock(5), null)
        }
    }
    
    test("should return a null-like default value for non reference types") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]
            val intToIntMock = mockFunction[Int, Int]

            intToIntMock.expects(*)
            assertEquals(intToIntMock(5), 0)
        }
    }
    
    test("should return what they're told to") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]
            val intToIntMock = mockFunction[Int, Int]

            intToStringMock.expects(*).returning("a return value")
            assertEquals(intToStringMock(5), "a return value")
        }
    }
    
    test("should return a calculated return value") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]
            val intToIntMock = mockFunction[Int, Int]

            intToIntMock.expects(*).onCall({ (arg) => arg + 1 })
            assertEquals(intToIntMock(42), 43)
        }
    }
    
    test("should return a calculated return value (chaining mocks)") {
        withExpectations() {
            val m1 = mockFunction[Int, String]
            val m2 = mockFunction[Int, String]

            m1.expects(42).onCall(m2)
            m2.expects(42).returning("a return value")

            assertEquals(m1(42), "a return value")
        }
    }
    
    test("should handle stacked expectations (returning)") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]

            intToStringMock.expects(*).returning("1")
            intToStringMock.expects(*).returning("2")

            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(2), "2")
        }
    }
    
    test("should handle stacked expectations (returning) and call count") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]
            val intToIntMock = mockFunction[Int, Int]

            intToStringMock.expects(*).returning("1").twice
            intToStringMock.expects(*).returning("2")

            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(1), "2")
        }
    }
    
    test("should handle stacked expectations (onCall)") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]
            val intToIntMock = mockFunction[Int, Int]

            intToStringMock.expects(*).onCall((_) => "1")
            intToStringMock.expects(*).onCall((_) => "2")

            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(1), "2")
        }
    }
    
    test("should handle stacked expectations (onCall) and call count") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]

            intToStringMock.expects(*).onCall((_) => "1").twice
            intToStringMock.expects(*).onCall((_) => "2")

            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(1), "2")          
        }
    }
    
    test("should match return value to provided arguments (returning)") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]

            intToStringMock.expects(1).onCall({ (_) => "1" })
            intToStringMock.expects(2).onCall({ (_) => "2" })

            assertEquals(intToStringMock(2), "2")
            assertEquals(intToStringMock(1), "1")
        }
    }
    
    test("should match return value to provided arguments (same order)") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]

            intToStringMock.expects(1).returning("1")
            intToStringMock.expects(2).returning("2")

            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(2), "2")
        }
    }
    
    test("should match return value to provided arguments (different order)") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]

            intToStringMock.expects(1).returning("1")
            intToStringMock.expects(2).returning("2")

            assertEquals(intToStringMock(2), "2")
            assertEquals(intToStringMock(1), "1")
        }
    }
    
    test("should match return value to provided arguments (call count)") {
        withExpectations() {
            val intToStringMock = mockFunction[Int, String]

            intToStringMock.expects(1).returning("1").twice
            intToStringMock.expects(2).returning("2")

            assertEquals(intToStringMock(1), "1")
            assertEquals(intToStringMock(2), "2")
            assertEquals(intToStringMock(1), "1")
        }
    }
}
