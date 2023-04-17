package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.scalatest.AsyncMockFactory
import eu.monniot.scala3mock.mockable.TestTrait
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.Future

/** Tests for mock defined in test case scope
  */
class BasicAsyncTest extends AsyncFlatSpec with Matchers with AsyncMockFactory {

  it should "work in scalatest async test" in {
    val mockedTrait = mock[TestTrait]
    when(() => mockedTrait.noParamMethod()).expects().returning("42")
    Future.successful {
      mockedTrait.noParamMethod() shouldBe "42"
    }
  }
}
