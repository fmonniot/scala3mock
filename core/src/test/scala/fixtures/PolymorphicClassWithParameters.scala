package fixtures

class PolymorphicClassWithParameters[F[_], E](
    a: HigherOrderPolymorphicTrait[F],
    n: Int
)(b: String) {

  def logic(stuff: String): F[Boolean] = a.doSomething(stuff.toInt)

}
