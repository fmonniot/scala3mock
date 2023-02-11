package fixtures

trait HigherOrderPolymorphicTrait[F[_]] {

  def doSomething(param: Int): F[Boolean]

}
