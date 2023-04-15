package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.MockFactory
import eu.monniot.scala3mock.mockable.TestTrait

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 *  Tests for mocks defined in fixture-contexts
 *
 *  Tests for issue #25
 */
class FixtureContextTest extends AnyFlatSpec with Matchers with MockFactory {

  trait TestSetup {
    val mockedTrait = mock[TestTrait]
    val input = 1
    val output = "one"
  }

  trait TestSetupWithExpectationsPredefined extends TestSetup {
    when(mockedTrait.oneParamMethod).expects(input).returning(output)
  }

  trait TestSetupWithHandlerCalledDuringInitialization extends TestSetupWithExpectationsPredefined {
    mockedTrait.oneParamMethod(input) shouldBe output
  }

  "ScalaTest suite" should "allow to use mock defined in fixture-context" in new TestSetup {
    when(mockedTrait.oneParamMethod).expects(input).returning(output)
    when(mockedTrait.oneParamMethod).expects(2).returning("two")

    mockedTrait.oneParamMethod(input) shouldBe output
    mockedTrait.oneParamMethod(2) shouldBe "two"
  }

  it should "allow to use mock defined in fixture-context with expectations predefined" in new TestSetupWithExpectationsPredefined {
    when(mockedTrait.oneParamMethod).expects(2).returning("two")

    mockedTrait.oneParamMethod(input) shouldBe output
    mockedTrait.oneParamMethod(2) shouldBe "two"
  }

  it should "allow mock defined in fixture-context to be used during context initialization" in new TestSetupWithHandlerCalledDuringInitialization {
    when(mockedTrait.oneParamMethod).expects(2).returning("two")

    mockedTrait.oneParamMethod(2) shouldBe "two"
  }
}