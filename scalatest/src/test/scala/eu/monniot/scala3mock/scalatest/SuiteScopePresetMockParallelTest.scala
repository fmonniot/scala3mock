package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.MockFactory
import eu.monniot.scala3mock.mockable.TestTrait

import org.scalatest.ParallelTestExecution
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.Ignore

/** Tests for mocks defined in suite scope (i.e. outside test case scope) with
  * predefined expectations
  */
@Ignore
class SuiteScopePresetMockParallelTest
    extends AnyFlatSpec
    with Matchers
    with MockFactory {

  val mockWithExpectationsPredefined = mock[TestTrait]
  /* TODO Currently throw NPE because context initialized on a test start
  when(mockWithExpectationsPredefined.oneParamMethod)
    .expects(0)
    .returning("predefined")
  */

  it should "allow to use mock defined suite scope with predefined expectations" in {
    when(mockWithExpectationsPredefined.oneParamMethod)
      .expects(1)
      .returning("one")

    mockWithExpectationsPredefined.oneParamMethod(0) shouldBe "predefined"
    mockWithExpectationsPredefined.oneParamMethod(1) shouldBe "one"
  }

  it should "keep predefined mock expectations" in {
    when(mockWithExpectationsPredefined.oneParamMethod)
      .expects(2)
      .returning("two")

    mockWithExpectationsPredefined.oneParamMethod(0) shouldBe "predefined"
    mockWithExpectationsPredefined.oneParamMethod(2) shouldBe "two"
  }
}
