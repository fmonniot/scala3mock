package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.MockFactory
import eu.monniot.scala3mock.mockable.TestTrait

import org.scalatest.ParallelTestExecution
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Tests for mocks defined in suite scope (i.e. outside test case scope). */
class SuiteScopeMockTest extends AnyFlatSpec with Matchers with MockFactory {

  val mockWithoutExpectationsPredefined = mock[TestTrait]

  it should "allow to use mock defined suite scope" in {
    when(mockWithoutExpectationsPredefined.oneParamMethod)
      .expects(1)
      .returning("one")
    when(mockWithoutExpectationsPredefined.oneParamMethod)
      .expects(2)
      .returning("two")

    mockWithoutExpectationsPredefined.oneParamMethod(1) shouldBe "one"
    mockWithoutExpectationsPredefined.oneParamMethod(2) shouldBe "two"
  }

  it should "allow to use mock defined suite scope in more than one test case" in {
    when(mockWithoutExpectationsPredefined.oneParamMethod)
      .expects(3)
      .returning("three")

    mockWithoutExpectationsPredefined.oneParamMethod(3) shouldBe "three"
  }
}
