package main

import context.{Call, Mock, MockContext}
import functions.{MockFunction, MockFunction1, MockFunction3}
import handlers.{CallHandler, Handler, UnorderedHandlers}
import macros.{mock, when}

import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

trait Foo:
  def foo(): Unit

trait TestTrait:
  def add(n: Int): Int = n + 1

class TestClass:
  def ac(number: Int): Int = number + 1

type TestTypeAlias = TestClass

trait TestMethodTypeParam:
  def ad[X](a: Int, b: X): Int

trait TestTypeParam[X]:
  def ae(a: Int, b: X): Int

trait TestTypeMethodTypeParam[X]:
  def af[Y, Y2](a: Int, b: X)(c: Y): Int

def functionUnderTest(dep: TestTrait, i: Int) = dep.add(i)


@main def main(): Unit =
  withExpectations() {
    val foo: Foo = mock[Foo]
    val tt = mock[TestTrait]
    val tc = mock[TestClass]
    val tta = mock[TestTypeAlias]

    //val tmtp = mock[TestMethodTypeParam]
    //val ttp = mock[TestTypeParam[String]]
    //val ttmtp = mock[TestTypeMethodTypeParam[String]]

    when(() => foo.foo())
      .expects()
      .returns(())

    when(tt.add)
      .expects(12)
      .atLeastOnce
      .returns(42)

    when(tc.ac)
      .expects(13)
      .atLeastOnce
      .returns(43)

    when(tta.ac)
      .expects(14)
      .atLeastOnce
      .returns(44)


    val r0: Unit = foo.foo()
    val r1 = functionUnderTest(tt, 12)
    val r2 = tc.ac(13)
    val r3 = tta.ac(14)

    println(summon[MockContext])
    println(s"r0 = $r0; r1 = $r1; r2 = $r2; r3 = $r3")
    assert(r0 == ())
    assert(r1 == 42)
    assert(r2 == 43)
    assert(r3 == 44)
  }









