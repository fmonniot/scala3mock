package fixtures

class PolymorphicClass[T] {
  def method[U](x: Int, y: T, z: U): String = ""

  trait Embedded[V] {
    trait ATrait[A, B]

    def innerTrait(t: T, v: V): ATrait[T, V]
    def outerTrait(t: T, v: V): PolymorphicClass.this.ATrait[T, V]
  }

  trait ATrait[A, B]
}
