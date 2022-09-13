package fixtures

import some.other.pkg.SomeClass

trait TestTrait {

  def nullary: String
  def noParams(): String
  def oneParam(x: Int): String
  def twoParams(x: Int, y: Double): String
  
  //def overloaded(x: Int): String
  //def overloaded(x: String): String
  //def overloaded(x: Int, y: Double): String
  //def overloaded[T](x: T): String
  
  def +(x: TestTrait): TestTrait
  
  def curried(x: Int)(y: Double): String
  //def curriedFuncReturn(x: Int): Double => String
  //def polymorphic[T](x: List[T]): String
  //def polycurried[T1, T2](x: T1)(y: T2): (T1, T2)
  //def polymorphicParam(x: (Int, Double)): String
  //def repeatedParam(x: Int, ys: String*): String
  //def byNameParam(x: => Int): String
  //def implicitParam(x: Int)(implicit y: Double): String
  
  //def upperBound[T <: Product](x: T): Int
  //def lowerBound[T >: U, U](x: T, y: List[U]): String
  //def contextBound[T: ContextBound](x: T): String
  
  //def withImplementation(x: Int) = x * x

  def referencesSomeOtherPackage(x: SomeClass): SomeClass
  //def otherPackageUpperBound[T <: SomeClass](x: T): T
  //def explicitPackageReference(x: yet.another.pkg.YetAnotherClass): yet.another.pkg.YetAnotherClass
  //def explicitPackageUpperBound[T <: yet.another.pkg.YetAnotherClass](x: T): T
  
  //var aVar: String
  //var concreteVar = "foo"

  //val aVal: String
  //val concreteVal = "foo"
  //val fnVal: String => Int
  
  /*
  trait Embedded {
    def m(x: Int, y: Double): String

    trait ATrait
    def innerTrait(): ATrait
    def outerTrait(): TestTrait.this.ATrait
    //def innerTraitProjected(): TestTrait#Embedded#ATrait
    //def outerTraitProjected(): TestTrait#ATrait
  }
  */
  
  //trait ATrait
  
  //def referencesEmbedded(): Embedded
}

/*
class TestClass {
    private def function = 0 // should not be part of the override
}
*/
