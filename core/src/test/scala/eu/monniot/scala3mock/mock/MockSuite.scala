package mock

import eu.monniot.scala3mock.main.withExpectations
import eu.monniot.scala3mock.functions.MockFunctions
import eu.monniot.scala3mock.main.TestExpectationEx
import eu.monniot.scala3mock.matchers.MatchAny
import eu.monniot.scala3mock.macros.*
import fixtures.*

class MockSuite extends munit.FunSuite with MockFunctions {

  def * = new MatchAny

  case class TestException() extends RuntimeException
  case class AnotherTestException() extends RuntimeException

  class Animal
  class Dog extends Animal

  test("PolymorphicClassWithParameters") {
    withExpectations() {
      val m = mock[PolymorphicClassWithParameters[List, TestException]]

      when(m.logic).expects("hello").returns(List(true))

      assertEquals(m.logic("hello"), List(true))
    }
  }

  test("TestTrait") {
    withExpectations() {
      val m = mock[TestTrait]

      // Nullary methods

      when(() => m.nullary).expects().returning("a")
      assertEquals(m.nullary, "a")

      when(() => m.noParams()).expects().returning("a2")
      assertEquals(m.noParams(), "a2")

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
      when(m.polymorphic _).expects(List(2)).returns("a1")
      assertEquals(m.polymorphic(List(2)), "a1")

      val javaObject = Object()
      when(() => m.polymorphicUnary).expects().returns(javaObject)
      assertEquals(m.polymorphicUnary[Object], javaObject)

      when(m.polycurried(_: String)(_: Int)).expects("a", 2).returns("c" -> 6)
      assertEquals(m.polycurried("a")(2), "c" -> 6)

      when(m.polymorphicParam).expects((1, 2.0)).returns("ok")
      assertEquals(m.polymorphicParam((1, 2.0)), "ok")

      when(m.repeatedParam _).expects(0, Seq.empty).returning("hello")
      assertEquals(m.repeatedParam(0), "hello")

      // TODO Not supported yet
      // when(m.byNameParam).expects(3).returns("ok")
      // assertEquals(m.byNameParam(3), "ok")

      // TODO Implicit/given parameters are not yet available. Waiting for
      // Scala 3 to let us do that.

      // type bound methods
      when(m.upperBound _).expects(TestException()).returns(1)
      assertEquals(m.upperBound(TestException()), 1)

      val animal = Animal()
      val dog = Dog()
      when(m.lowerBound).expects(animal, List(dog)).returns("ok")
      assertEquals(m.lowerBound(animal, List(dog)), "ok")

      // TODO with implementation methods

      // TODO package references methods

      // TODO values

      // TODO embedded trait
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

  test("ManyParams") {
    withExpectations() {
      // TODO Missing MockFunction impl for 3+ parameters
      // val a = mock[ManyParamsClass]
      // val b = mock[ManyParamsTrait]

      // TODO assertions
    }
  }

  test("PolymorphicClass") {
    withExpectations() {
      val c = mock[PolymorphicClass[String]]

      // TODO assertions
    }
  }

  test("PolymorphicTrait") {
    withExpectations() {
      val d = mock[PolymorphicTrait[Int]]

      // TODO assertions
    }
  }

  test("SpecializedClass".ignore) {
    withExpectations() {
      // Seems to be special case for scala.js ? Not entirely sure though
      // val f = mock[SpecializedClass2[Int, Float]]
      // I suppose that mean we can skip those for now.
      val e = mock[SpecializedClass[Int]]

      // TODO assertions
    }
  }

  test("TestClass") {
    withExpectations() {
      val g = mock[TestClass]

      // TODO assertions
    }
  }
}
