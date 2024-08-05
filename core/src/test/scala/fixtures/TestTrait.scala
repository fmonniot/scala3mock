package fixtures

import some.other.pkg.SomeClass

trait TestTrait {

  def nullary: String
  def noParams(): String
  def oneParam(x: Int): String
  def twoParams(x: Int, y: Double): String

  def overloaded(x: Int): String
  def overloaded(x: String): String
  def overloaded(x: Int, y: Double): String
  def overloaded[T](x: T): String

  def +(x: TestTrait): TestTrait

  def curried(x: Int)(y: Double): String
  def curriedFuncReturn(x: Int): Double => String

  def polymorphic[T](x: List[T]): String
  def polymorphicUnary[T1]: T1
  def polycurried[T1, T2](x: T1)(y: T2): (T1, T2)
  def polymorphicParam(x: (Int, Double)): String
  def repeatedParam(x: Int, ys: String*): String
  def byNameParam(x: => Int): String

  def implicitParam(x: Int)(implicit y: Double): String
  def usingParam(x: Int)(using y: Double): String
  def contextBound[T: ContextBound](x: T): String

  def upperBound[T <: Product](x: T): Int
  def lowerBound[T >: U, U](x: T, y: List[U]): String

  def withImplementation(x: Int) = x * x

  def referencesSomeOtherPackage(x: SomeClass): SomeClass
  def otherPackageUpperBound[T <: SomeClass](x: T): T
  def explicitPackageReference(
      x: yet.another.pkg.YetAnotherClass
  ): yet.another.pkg.YetAnotherClass
  def explicitPackageUpperBound[T <: yet.another.pkg.YetAnotherClass](x: T): T

  // doesn't seems to be possible with Scala 3.
  // We get an error saying "error overriding variable [...] cannot override a mutable variable"
  // var aVar: String
  // var concreteVar = "foo"

  // Here all we can do is making those non-abstract so the mock can compile.
  // By definition, there is no way to dynamically change the value of a val.
  val aVal: String
  val concreteVal = "foo"
  // val fnVal: String => Int
}
