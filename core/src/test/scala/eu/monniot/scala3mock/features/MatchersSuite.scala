package features

import eu.monniot.scala3mock.ScalaMocks

class MatchersSuite extends munit.FunSuite with ScalaMocks {

  test("should let you use star for MatchAny") {
    withExpectations() {
      val intToStringMock = mockFunction[Int, String]

      intToStringMock.expects(*)
      assertEquals(intToStringMock(5), null)
    }
  }

  test("should let you use tilde for MatchEpsilon") {
    withExpectations() {
      val intToStringMock = mockFunction[Double, String]

      intToStringMock.expects(~2.0)
      assertEquals(intToStringMock(2.001), null)
    }
  }
}
