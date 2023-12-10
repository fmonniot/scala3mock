package eu.monniot.scala3mock.cats

import cats.effect.SyncIO

class CatsSuite extends munit.FunSuite with ScalaMocks {

  test("it should validate expectations after a lazy data type evaluated") {
    // An implicit assumption of this code block is that the expectations
    // are not validated on exit of the `withExpectations` function but when
    // the returned Monad is being evaluated.
    val fa = withExpectations() {
      val intToStringMock = mockFunction[Int, String]
      intToStringMock.expects(*)

      SyncIO(intToStringMock(2))
    }

    assertEquals(fa.unsafeRunSync(), null)
  }

}
