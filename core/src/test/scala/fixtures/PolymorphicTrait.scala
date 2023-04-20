package fixtures

trait PolymorphicTrait[T] {
  def method[U](x: Int, y: T, z: U): T

  trait Embedded[V] {
    trait ATrait[A, B]

    def innerTrait(t: T, v: V): ATrait[T, V]
    def outerTrait(t: T, v: V): PolymorphicTrait.this.ATrait[T, V]
  }

  trait ATrait[A, B]
}
