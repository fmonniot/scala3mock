package mock

import eu.monniot.scala3mock.main.withExpectations
import eu.monniot.scala3mock.functions.MockFunctions
import eu.monniot.scala3mock.main.TestExpectationEx
import eu.monniot.scala3mock.matchers.MatchAny
import eu.monniot.scala3mock.macros.*
import fixtures.*
import eu.monniot.scala3mock.handlers.CallHandler1
import eu.monniot.scala3mock.functions.MockFunction1
import eu.monniot.scala3mock.context.MockContext
import eu.monniot.scala3mock.main.Default

class MockSuite extends munit.FunSuite with MockFunctions {

  def * = new MatchAny

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
      // https://github.com/fmonniot/scala3mock/issues/4
      // when(m.byNameParam).expects(3).returns("ok")
      // assertEquals(m.byNameParam(3), "ok")

      // implicit parameters
      given y: Double = 1.23
      when(m.implicitParam(_: Int)(_: Double))
        .expects(42, 1.23)
        .returning("it works")
      assertEquals(m.implicitParam(42), "it works")

      // type bound methods
      when(m.upperBound _).expects(TestException()).returns(1)
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

  test("ManyParams") {
    withExpectations() {
      // TODO Missing MockFunction impl for 3+ parameters
      // val a = mock[ManyParamsClass]
      // val b = mock[ManyParamsTrait]

      // TODO assertions
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

  test("SpecializedClass".ignore) {
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
      assertEquals(c.identity2(42, 43), (44,45))

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
}
