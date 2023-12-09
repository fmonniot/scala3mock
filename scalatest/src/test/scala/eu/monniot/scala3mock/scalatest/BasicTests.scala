package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.MockFactory
import eu.monniot.scala3mock.mockable.TestTrait
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Tests for mock defined in test case scope
  *
  * Tests for issue #25
  */
class BasicTest extends AnyFlatSpec with Matchers with MockFactory {

  it should "allow to use mock defined in test case scope" in {
    val mockedTrait = mock[TestTrait]
    when(mockedTrait.oneParamMethod).expects(*).returning("one")
    when(mockedTrait.oneParamMethod).expects(2).returning("two")
    when(() => mockedTrait.noParamMethod()).expects().returning("yey")

    mockedTrait.oneParamMethod(1) shouldBe "one"
    mockedTrait.oneParamMethod(2) shouldBe "two"
    mockedTrait.noParamMethod() shouldBe "yey"
  }

  it should "use separate call logs for each test case" in {
    val mockedTrait = mock[TestTrait]
    when(mockedTrait.oneParamMethod).expects(3).returning("three")

    mockedTrait.oneParamMethod(3) shouldBe "three"
  }
}
