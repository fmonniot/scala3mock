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

      when(m.contextBound(_: String)(_: ContextBound[String]))
        .expects("arg", cb)
        .returning("ok")
      assertEquals(m.contextBound("arg"), "ok")

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

  test("ManyParamsClass") {
    withExpectations() {
      val m = mock[ManyParamsClass]

      when(m.methodWith1Ints).expects(1).returns(1)
      assertEquals(m.methodWith1Ints(1), 1)

      when(m.methodWith2Ints).expects(1, 1).returns(2)
      assertEquals(m.methodWith2Ints(1, 1), 2)

      when(m.methodWith3Ints).expects(1, 1, 1).returns(3)
      assertEquals(m.methodWith3Ints(1, 1, 1), 3)

      when(m.methodWith4Ints).expects(1, 1, 1, 1).returns(4)
      assertEquals(m.methodWith4Ints(1, 1, 1, 1), 4)

      when(m.methodWith5Ints).expects(1, 1, 1, 1, 1).returns(5)
      assertEquals(m.methodWith5Ints(1, 1, 1, 1, 1), 5)

      when(m.methodWith6Ints).expects(1, 1, 1, 1, 1, 1).returns(6)
      assertEquals(m.methodWith6Ints(1, 1, 1, 1, 1, 1), 6)

      when(m.methodWith7Ints).expects(1, 1, 1, 1, 1, 1, 1).returns(7)
      assertEquals(m.methodWith7Ints(1, 1, 1, 1, 1, 1, 1), 7)

      when(m.methodWith8Ints).expects(1, 1, 1, 1, 1, 1, 1, 1).returns(8)
      assertEquals(m.methodWith8Ints(1, 1, 1, 1, 1, 1, 1, 1), 8)

      when(m.methodWith9Ints).expects(1, 1, 1, 1, 1, 1, 1, 1, 1).returns(9)
      assertEquals(m.methodWith9Ints(1, 1, 1, 1, 1, 1, 1, 1, 1), 9)

      when(m.methodWith10Ints).expects(1, 1, 1, 1, 1, 1, 1, 1, 1, 1).returns(10)
      assertEquals(m.methodWith10Ints(1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 10)
    }
  }

  test("ManyParams") {
    withExpectations() {
      val m = mock[ManyParamsTrait]

      when(m.methodWith1Ints).expects(1).returns(1)
      assertEquals(m.methodWith1Ints(1), 1)

      when(m.methodWith2Ints).expects(1, 1).returns(2)
      assertEquals(m.methodWith2Ints(1, 1), 2)

      when(m.methodWith3Ints).expects(1, 1, 1).returns(3)
      assertEquals(m.methodWith3Ints(1, 1, 1), 3)

      when(m.methodWith4Ints).expects(1, 1, 1, 1).returns(4)
      assertEquals(m.methodWith4Ints(1, 1, 1, 1), 4)

      when(m.methodWith5Ints).expects(1, 1, 1, 1, 1).returns(5)
      assertEquals(m.methodWith5Ints(1, 1, 1, 1, 1), 5)

      when(m.methodWith6Ints).expects(1, 1, 1, 1, 1, 1).returns(6)
      assertEquals(m.methodWith6Ints(1, 1, 1, 1, 1, 1), 6)

      when(m.methodWith7Ints).expects(1, 1, 1, 1, 1, 1, 1).returns(7)
      assertEquals(m.methodWith7Ints(1, 1, 1, 1, 1, 1, 1), 7)

      when(m.methodWith8Ints).expects(1, 1, 1, 1, 1, 1, 1, 1).returns(8)
      assertEquals(m.methodWith8Ints(1, 1, 1, 1, 1, 1, 1, 1), 8)

      when(m.methodWith9Ints).expects(1, 1, 1, 1, 1, 1, 1, 1, 1).returns(9)
      assertEquals(m.methodWith9Ints(1, 1, 1, 1, 1, 1, 1, 1, 1), 9)

      when(m.methodWith10Ints).expects(1, 1, 1, 1, 1, 1, 1, 1, 1, 1).returns(10)
      assertEquals(m.methodWith10Ints(1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 10)
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
}
