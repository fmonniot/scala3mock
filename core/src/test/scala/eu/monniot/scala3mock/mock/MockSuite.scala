package mock

import eu.monniot.scala3mock.ScalaMocks
import eu.monniot.scala3mock.Default
import fixtures.*

class MockSuite extends munit.FunSuite with ScalaMocks {

  case class TestException() extends RuntimeException
  case class AnotherTestException() extends RuntimeException

  class Animal
  class Dog extends Animal

  test("TestTrait") {
    withExpectations() {
      val m = mock[TestTrait]

      // Simple methods

      when(m.oneParam).expects(42).returns("ok")
      assertEquals(m.oneParam(42), "ok")

      when(m.twoParams).expects(42, 42.0).returns("ok")
      assertEquals(m.twoParams(42, 42.0), "ok")

      // Overloaded methods

      when(m.overloaded(_: Int)).expects(1).returning("b")
      assertEquals(m.overloaded(1), "b")

      when(m.overloaded(_: String)).expects("a").returning("c")
      assertEquals(m.overloaded("a"), "c")

      when(m.overloaded(_: Int, _: Double)).expects(3, 4.2).returning("d")
      assertEquals(m.overloaded(3, 4.2), "d")

      when(m.overloaded(_: Unit)).expects(()).returning("e")
      assertEquals(m.overloaded(()), "e")

      // Operator methods
      when(m.`+`).expects(m).returns(m)
      assertEquals(m + m, m)

      // Curried methods

      when(m.curried(_: Int)(_: Double)).expects(1, 2.1).returns("ok")
      assertEquals(m.curried(1)(2.1), "ok")

      val doubleToString: Double => String = _ => "ok"
      when(m.curriedFuncReturn).expects(2).returns(doubleToString)
      assertEquals(m.curriedFuncReturn(2), doubleToString)

      // Polymorphic methods
      when(m.polymorphic).expects(List(2)).returns("a1")
      assertEquals(m.polymorphic(List(2)), "a1")

      val javaObject = Object()
      when(() => m.polymorphicUnary).expects().returns(javaObject)
      assertEquals(m.polymorphicUnary[Object], javaObject)

      when(m.polycurried(_: String)(_: Int)).expects("a", 2).returns("c" -> 6)
      assertEquals(m.polycurried("a")(2), "c" -> 6)

      when(m.polymorphicParam).expects((1, 2.0)).returns("ok")
      assertEquals(m.polymorphicParam((1, 2.0)), "ok")

      when(m.repeatedParam).expects(0, Seq.empty).returning("hello")
      assertEquals(m.repeatedParam(0), "hello")

      // Partially supported. Issue with type inference still. See https://github.com/fmonniot/scala3mock/issues/4
      // Interestingly enough here if we do not force the types, the compiler will happily
      // infer a type `((=> Int) => String)` which doesn't matches any of our `when` declarations.
      // This can also be fixed by having the when/WhenImpl macros takes a by-name parameter. That
      // second solution isn't very practical for 3+ matchers as it would explode the number of overload
      // required to match all possible cases.
      when[Int, String](m.byNameParam).expects(3).returns("ok")
      assertEquals(m.byNameParam(3), "ok")

      // implicit parameters
      given y: Double = 1.23
      when(m.implicitParam(_: Int)(_: Double))
        .expects(42, 1.23)
        .returning("it works")
      assertEquals(m.implicitParam(42), "it works")

      when(m.usingParam(_: Int)(using _: Double))
        .expects(43, 1.23)
        .returning("ok")
      assertEquals(m.usingParam(43), "ok")

      given cb: ContextBound[String] = new ContextBound {}

      when(m.contextBound(_: String)(using _: ContextBound[String]))
        .expects("arg", cb)
        .returning("ok")
      assertEquals(m.contextBound("arg"), "ok")

      // type bound methods
      when(m.upperBound).expects(TestException()).returns(1)
      assertEquals(m.upperBound(TestException()), 1)

      val animal = Animal()
      val dog = Dog()
      when(m.lowerBound).expects(animal, List(dog)).returns("ok")
      assertEquals(m.lowerBound(animal, List(dog)), "ok")

      // non-abstract methods
      when(m.withImplementation).expects(42).returning(1234)
      assertEquals(m.withImplementation(42), 1234)
    }
  }

  test("TestTrait - onCall unary") {
    withExpectations() {
      val m = mock[TestTrait]

      // Nullary methods

      when(() => m.nullary).expects().onCall(() => "a")
      assertEquals(m.nullary, "a")

      when(() => m.noParams()).expects().onCall(() => "a2")
      assertEquals(m.noParams(), "a2")
    }
  }

  test("ManyParamsClass") {
    withExpectations() {
      val m = mock[ManyParamsClass]

      val result1 = 1
      when(m.methodWith1Ints).expects(1).returns(result1)
      assertEquals(m.methodWith1Ints(1), result1)

      val result2 = result1 + 2
      when(m.methodWith2Ints).expects(1, 2).returns(result2)
      assertEquals(m.methodWith2Ints(1, 2), result2)

      val result3 = result2 + 3
      when(m.methodWith3Ints).expects(1, 2, 3).returns(result3)
      assertEquals(m.methodWith3Ints(1, 2, 3), result3)

      val result4 = result3 + 4
      when(m.methodWith4Ints).expects(1, 2, 3, 4).returns(result4)
      assertEquals(m.methodWith4Ints(1, 2, 3, 4), result4)

      val result5 = result4 + 5
      when(m.methodWith5Ints).expects(1, 2, 3, 4, 5).returns(result5)
      assertEquals(m.methodWith5Ints(1, 2, 3, 4, 5), result5)

      val result6 = result5 + 6
      when(m.methodWith6Ints).expects(1, 2, 3, 4, 5, 6).returns(result6)
      assertEquals(m.methodWith6Ints(1, 2, 3, 4, 5, 6), result6)

      val result7 = result6 + 7
      when(m.methodWith7Ints).expects(1, 2, 3, 4, 5, 6, 7).returns(result7)
      assertEquals(m.methodWith7Ints(1, 2, 3, 4, 5, 6, 7), result7)

      val result8 = result7 + 8
      when(m.methodWith8Ints).expects(1, 2, 3, 4, 5, 6, 7, 8).returns(result8)
      assertEquals(m.methodWith8Ints(1, 2, 3, 4, 5, 6, 7, 8), result8)

      val result9 = result8 + 9
      when(m.methodWith9Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .returns(result9)
      assertEquals(m.methodWith9Ints(1, 2, 3, 4, 5, 6, 7, 8, 9), result9)

      val result10 = result9 + 10
      when(m.methodWith10Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .returns(result10)
      assertEquals(m.methodWith10Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result10)

      val result11 = result10 + 11
      when(m.methodWith11Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        .returns(result11)
      assertEquals(
        m.methodWith11Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
        result11
      )

      val result12 = result11 + 12
      when(m.methodWith12Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        .returns(result12)
      assertEquals(
        m.methodWith12Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
        result12
      )

      val result13 = result12 + 13
      when(m.methodWith13Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
        .returns(result13)
      assertEquals(
        m.methodWith13Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
        result13
      )

      val result14 = result13 + 14
      when(m.methodWith14Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
        .returns(result14)
      assertEquals(
        m.methodWith14Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
        result14
      )

      val result15 = result14 + 15
      when(m.methodWith15Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        .returns(result15)
      assertEquals(
        m.methodWith15Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
        result15
      )

      val result16 = result15 + 16
      when(m.methodWith16Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        .returns(result16)
      assertEquals(
        m.methodWith16Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16),
        result16
      )

      val result17 = result16 + 17
      when(m.methodWith17Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
        .returns(result17)
      assertEquals(
        m.methodWith17Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17),
        result17
      )

      val result18 = result17 + 18
      when(m.methodWith18Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
        .returns(result18)
      assertEquals(
        m.methodWith18Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18),
        result18
      )

      val result19 = result18 + 19
      when(m.methodWith19Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19)
        .returns(result19)
      assertEquals(
        m.methodWith19Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19),
        result19
      )

      val result20 = result19 + 20
      when(m.methodWith20Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19, 20)
        .returns(result20)
      assertEquals(
        m.methodWith20Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19, 20),
        result20
      )

      val result21 = result20 + 21
      when(m.methodWith21Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19, 20, 21)
        .returns(result21)
      assertEquals(
        m.methodWith21Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19, 20, 21),
        result21
      )

      val result22 = result21 + 22
      when(m.methodWith22Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19, 20, 21, 22)
        .returns(result22)
      assertEquals(
        m.methodWith22Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19, 20, 21, 22),
        result22
      )
    }
  }

  test("Match predicate") {
    withExpectations() {
      // format: off
      val mock1 = mockFunction[Int, Unit]
      mock1.expects(where {
        (v1: Int) => v1 == 1
      }).returning(())
      mock1(1)

      val mock2 = mockFunction[Int, Int, Unit]
      mock2.expects(where {
        (v1: Int, v2: Int) => v1 == 1 && v2 == 2
      }).returning(())
      mock2(1, 2)

      val mock3 = mockFunction[Int, Int, Int, Unit]
      mock3.expects(where {
        (v1: Int, v2: Int, v3: Int) => v1 == 1 && v2 == 2 && v3 == 3
      }).returning(())
      mock3(1, 2, 3)

      val mock4 = mockFunction[Int, Int, Int, Int, Unit]
      mock4.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int) => v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4
      }).returning(())
      mock4(1, 2, 3, 4)

      val mock5 = mockFunction[Int, Int, Int, Int, Int, Unit]
      mock5.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int) => v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5
      }).returning(())
      mock5(1, 2, 3, 4, 5)

      val mock6 = mockFunction[Int, Int, Int, Int, Int, Int, Unit]
      mock6.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int) => v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6
      }).returning(())
      mock6(1, 2, 3, 4, 5, 6)

      val mock7 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Unit]
      mock7.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int) => v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7
      }).returning(())
      mock7(1, 2, 3, 4, 5, 6, 7)

      val mock8 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock8.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int) => v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8
      }).returning(())
      mock8(1, 2, 3, 4, 5, 6, 7, 8)

      val mock9 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock9.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9
      }).returning(())
      mock9(1, 2, 3, 4, 5, 6, 7, 8, 9)

      val mock10 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock10.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10
      }).returning(())
      mock10(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

      val mock11 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock11.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11
      }).returning(())
      mock11(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)

      val mock12 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock12.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12
      }).returning(())
      mock12(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

      val mock13 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock13.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13
      }).returning(())
      mock13(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)

      val mock14 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock14.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14
      }).returning(())
      mock14(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)

      val mock15 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock15.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15
      }).returning(())
      mock15(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)

      val mock16 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock16.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int, v16: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15 && v16 == 16
      }).returning(())
      mock16(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)

      val mock17 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock17.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int, v16: Int, v17: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15 && v16 == 16 && v17 == 17
      }).returning(())
      mock17(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)

      val mock18 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock18.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int, v16: Int, v17: Int, v18: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15 && v16 == 16 && v17 == 17 && v18 == 18
      }).returning(())
      mock18(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)

      val mock19 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock19.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int, v16: Int, v17: Int, v18: Int, v19: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15 && v16 == 16 && v17 == 17 && v18 == 18 && v19 == 19
      }).returning(())
      mock19(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)

      val mock20 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock20.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int, v16: Int, v17: Int, v18: Int, v19: Int, v20: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15 && v16 == 16 && v17 == 17 && v18 == 18 && v19 == 19 && v20 == 20
      }).returning(())
      mock20(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)

      val mock21 = mockFunction[Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Unit]
      mock21.expects(where {
        (v1: Int, v2: Int, v3: Int, v4: Int, v5: Int, v6: Int, v7: Int, v8: Int, v9: Int, v10: Int, v11: Int, v12: Int, v13: Int, v14: Int, v15: Int, v16: Int, v17: Int, v18: Int, v19: Int, v20: Int, v21: Int) =>
          v1 == 1 && v2 == 2 && v3 == 3 && v4 == 4 && v5 == 5 && v6 == 6 && v7 == 7 && v8 == 8 && v9 == 9 && v10 == 10 && v11 == 11 && v12 == 12 && v13 == 13 &&
            v14 == 14 && v15 == 15 && v16 == 16 && v17 == 17 && v18 == 18 && v19 == 19 && v20 == 20 && v21 == 21
      }).returning(())
      mock21(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21)
      // format: off
    }
  }

  test("ManyParams") {
    withExpectations() {
      val m = mock[ManyParamsTrait]

      val result1 = 1
      when(m.methodWith1Ints).expects(1).returns(result1)
      assertEquals(m.methodWith1Ints(1), result1)

      val result2 = result1 + 2
      when(m.methodWith2Ints).expects(1, 2).returns(result2)
      assertEquals(m.methodWith2Ints(1, 2), result2)

      val result3 = result2 + 3
      when(m.methodWith3Ints).expects(1, 2, 3).returns(result3)
      assertEquals(m.methodWith3Ints(1, 2, 3), result3)

      val result4 = result3 + 4
      when(m.methodWith4Ints).expects(1, 2, 3, 4).returns(result4)
      assertEquals(m.methodWith4Ints(1, 2, 3, 4), result4)

      val result5 = result4 + 5
      when(m.methodWith5Ints).expects(1, 2, 3, 4, 5).returns(result5)
      assertEquals(m.methodWith5Ints(1, 2, 3, 4, 5), result5)

      val result6 = result5 + 6
      when(m.methodWith6Ints).expects(1, 2, 3, 4, 5, 6).returns(result6)
      assertEquals(m.methodWith6Ints(1, 2, 3, 4, 5, 6), result6)

      val result7 = result6 + 7
      when(m.methodWith7Ints).expects(1, 2, 3, 4, 5, 6, 7).returns(result7)
      assertEquals(m.methodWith7Ints(1, 2, 3, 4, 5, 6, 7), result7)

      val result8 = result7 + 8
      when(m.methodWith8Ints).expects(1, 2, 3, 4, 5, 6, 7, 8).returns(result8)
      assertEquals(m.methodWith8Ints(1, 2, 3, 4, 5, 6, 7, 8), result8)

      val result9 = result8 + 9
      when(m.methodWith9Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .returns(result9)
      assertEquals(m.methodWith9Ints(1, 2, 3, 4, 5, 6, 7, 8, 9), result9)

      val result10 = result9 + 10
      when(m.methodWith10Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .returns(result10)
      assertEquals(m.methodWith10Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), result10)

      val result11 = result10 + 11
      when(m.methodWith11Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        .returns(result11)
      assertEquals(
        m.methodWith11Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
        result11
      )

      val result12 = result11 + 12
      when(m.methodWith12Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        .returns(result12)
      assertEquals(
        m.methodWith12Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
        result12
      )

      val result13 = result12 + 13
      when(m.methodWith13Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
        .returns(result13)
      assertEquals(
        m.methodWith13Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
        result13
      )

      val result14 = result13 + 14
      when(m.methodWith14Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
        .returns(result14)
      assertEquals(
        m.methodWith14Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
        result14
      )

      val result15 = result14 + 15
      when(m.methodWith15Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        .returns(result15)
      assertEquals(
        m.methodWith15Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
        result15
      )

      val result16 = result15 + 16
      when(m.methodWith16Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        .returns(result16)
      assertEquals(
        m.methodWith16Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16),
        result16
      )

      val result17 = result16 + 17
      when(m.methodWith17Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
        .returns(result17)
      assertEquals(
        m.methodWith17Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17),
        result17
      )

      val result18 = result17 + 18
      when(m.methodWith18Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
        .returns(result18)
      assertEquals(
        m.methodWith18Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18),
        result18
      )

      val result19 = result18 + 19
      when(m.methodWith19Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19)
        .returns(result19)
      assertEquals(
        m.methodWith19Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19),
        result19
      )

      val result20 = result19 + 20
      when(m.methodWith20Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19, 20)
        .returns(result20)
      assertEquals(
        m.methodWith20Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19, 20),
        result20
      )

      val result21 = result20 + 21
      when(m.methodWith21Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19, 20, 21)
        .returns(result21)
      assertEquals(
        m.methodWith21Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19, 20, 21),
        result21
      )

      val result22 = result21 + 22
      when(m.methodWith22Ints)
        .expects(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
          19, 20, 21, 22)
        .returns(result22)
      assertEquals(
        m.methodWith22Ints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
          16, 17, 18, 19, 20, 21, 22),
        result22
      )
    }
  }

  test("PolymorphicTrait") {
    withExpectations() {
      val d = mock[PolymorphicTrait[Int]]

      // embedded trait and path-dependent types
      // https://github.com/fmonniot/scala3mock/issues/3
      /*
      val e = mock[m.Embedded]
      when(() => m.referenceEmbedded()).expects().returning(e)
      assertEquals(m.referenceEmbedded(), e)


      val o = mock[m.ATrait]
      val i = mock[e.ATrait]
      when(() => e.innerTrait()).expects().returning(i)
      when(() => e.outerTrait()).expects().returning(o)
      assertEquals(e.outerTrait(), o)
      assertEquals(e.innerTrait(), i)
       */
    }
  }

  test("PolymorphicClassWithParameters") {
    withExpectations() {
      val c = mock[PolymorphicClassWithParameters[List, Int]]

      when(c.logic).expects("param").returns(List(true))
      assertEquals(c.logic("param"), List(true))
    }
  }

  test("SpecializedClass") {
    withExpectations() {
      val a = mock[SpecializedClass[Int]]
      when(a.identity).expects(42).returns(43)
      assertEquals(a.identity(42), 43)

      val b = mock[SpecializedClass2[Int, String]]
      when(b.identity2).expects(42, "43").returns((44, "45"))
      assertEquals(b.identity2(42, "43"), (44, "45"))

      when(b.identity).expects(42).returns(44)
      assertEquals(b.identity(42), 44)

      val c = mock[SpecializedClass2[Int, Int]]
      when(c.identity2).expects(42, 43).returns((44, 45))
      assertEquals(c.identity2(42, 43), (44, 45))

      when(c.identity).expects(42).returns(44)
      assertEquals(c.identity(42), 44)

      val d = mock[SpecializedClass[String]]
      when(d.identity).expects("one").returns("two")
      assertEquals(d.identity("one"), "two")

      val e = mock[SpecializedClass[List[String]]]
      when(e.identity).expects(List("one")).returns(List("two"))
      assertEquals(e.identity(List("one")), List("two"))
    }
  }

  test("TestClass") {
    withExpectations() {
      val g = mock[TestClass[List]]

      when(g.a).expects(1, "str").returns(4 -> "ok")
      assertEquals(g.a(1, "str"), 4 -> "ok")

      when(g.b).expects(1).returns(List(4))
      assertEquals(g.b(1), List(4))
    }
  }

  test("ClassWithoutTypeParameters") {
    withExpectations() {
      val m = mock[ClassWithoutTypeParameters]
      assert(m.isInstanceOf[ClassWithoutTypeParameters])
    }
  }

  test("PrintStream - class with private constructor - issue #15") {
    withExpectations() {
      val m = mock[java.io.PrintStream]

      when(m.print(_: String)).expects("hello")
      m.print("hello")
    }
  }

  test("User's can define their own Default value") {
    case class MyType(s: String)
    given Default[MyType] with { val default = MyType("testing") }

    trait MyService {
      def service(): MyType
    }

    withExpectations() {
      val m = mock[MyService]

      when(() => m.service()).expects()

      assertEquals(m.service(), MyType("testing"))
    }
  }

  test(
    "parameterized trait ContextBoundInheritance is indirectly implemented - issue #48"
  ) {
    withExpectations() {
      mock[ContextBoundInheritanceChild[List]]
    }
  }

  test("TestDefaultParameters - issue #53") {
    withExpectations() {
      val m = mock[TestDefaultParameters]

      when(m.foo).expects(9).returns("ok")
      when(m.foo).expects(42).returns("ok2")

      when(m.multiParamList(_: Int, _: Int)(_: Long, _: Long))
        .expects(1, 1, 1, 1)
        .returns("one")
      when(m.multiParamList(_: Int, _: Int)(_: Long, _: Long))
        .expects(1, 0, 1, 0)
        .returns("default")

      assertEquals(m.foo(), "ok")
      assertEquals(m.foo(42), "ok2")

      assertEquals(m.multiParamList(1, 1)(1, 1), "one")
      assertEquals(m.multiParamList(1)(1), "default")
    }
  }

  test("overloaded private method - issue #68") {
    class Service {
      def method(value: String): String = method(2, true)
      private def method(value: Int, value2: Boolean): String = "nok"
    }

    withExpectations() {
      val service = mock[Service]

      when(service.method).expects("").returns("ok")
      assertEquals(service.method(""), "ok")
    }
  }
}
