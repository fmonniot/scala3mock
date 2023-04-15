package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.MockFactory
import eu.monniot.scala3mock.mockable.TestTrait
import org.scalatest._
import org.scalatest.exceptions.TestFailedException

import scala.language.postfixOps
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Tests that errors are reported correctly in ScalaTest suites
  */
class ErrorReportingTest
    extends AnyFlatSpec
    with Matchers
    with TestSuiteRunner {

  it should "report unexpected call correctly" in {
    class TestedSuite extends AnyFunSuite with MockFactory {
      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        mockedTrait.oneParamMethod(3)
      }
    }

    val outcome = runTestCase[TestedSuite](new TestedSuite)
    val errorMessage = getErrorMessage[TestFailedException](outcome)
    errorMessage should startWith("Unexpected call")
  }

  it should "report unexpected call correctly when expectations are set" in {
    class TestedSuite extends AnyFunSuite with MockFactory {
      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        when(mockedTrait.oneParamMethod).expects(1).returning("one")
        mockedTrait.oneParamMethod(3)
      }
    }

    // Unexpected call should be reported by ScalaTest
    val outcome = runTestCase[TestedSuite](new TestedSuite)
    val errorMessage = getErrorMessage[TestFailedException](outcome)
    errorMessage should startWith("Unexpected call")
  }

  it should "not hide NullPointerException" in {
    class TestedSuite extends AnyFunSuite with MockFactory {
      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        when(mockedTrait.oneParamMethod).expects(1).returning("one")
        throw new NullPointerException;
      }
    }

    val outcome = runTestCase[TestedSuite](new TestedSuite)
    // NullPointerException should be reported by ScalaTest
    getThrowable[NullPointerException](outcome) shouldBe a[NullPointerException]
  }

  it should "report default mock names" in pendingUntilFixed {
    class TestedSuite extends AnyFunSuite with MockFactory {
      test("execute block of code") {
        val mockA = mock[TestTrait]
        val mockB = mock[TestTrait]

        when(mockA.oneParamMethod).expects(3)
        mockB.oneParamMethod(3)
      }
    }

    val outcome = runTestCase[TestedSuite](new TestedSuite)
    val errorMessage = getErrorMessage[TestFailedException](outcome)
    errorMessage shouldBe
      """|Unexpected call: <mock-2> TestTrait.oneParamMethod(3)
        |
        |Expected:
        |inAnyOrder {
        |  <mock-1> TestTrait.oneParamMethod(3) once (never called - UNSATISFIED)
        |}
        |
        |Actual:
        |  <mock-2> TestTrait.oneParamMethod(3)
      """.stripMargin.trim
  }

  it should "report unexpected calls in readable manner" in pendingUntilFixed {
    trait OuterTestTrait
        extends TestTrait // To have a different name being reported
    class TestedSuite extends AnyFunSuite with MockFactory {
      val suiteScopeMock = mock[OuterTestTrait]
      when(() => suiteScopeMock.noParamMethod())
        .expects()
        .returning("two")
        .twice

      test("execute block of code") {
        val mockedTrait = mock[TestTrait]
        when(mockedTrait.polymorphicMethod).expects(List(1)).returning("one")

        suiteScopeMock.noParamMethod()
        mockedTrait.oneParamMethod(3)
      }
    }

    val outcome = runTestCase[TestedSuite](new TestedSuite)
    val errorMessage = getErrorMessage[TestFailedException](outcome)
    errorMessage shouldBe
      """|Unexpected call: <mock-1> TestTrait.oneParamMethod(3)
         |
         |Expected:
         |inAnyOrder {
         |  <suite mock> TestTrait.noParamMethod() twice (called once - UNSATISFIED)
         |  <mock-1> TestTrait.polymorphicMethod[T](List(1)) once (never called - UNSATISFIED)
         |}
         |
         |Actual:
         |  <suite mock> TestTrait.noParamMethod()
         |  <mock-1> TestTrait.oneParamMethod(3)
      """.stripMargin.trim
  }
}
