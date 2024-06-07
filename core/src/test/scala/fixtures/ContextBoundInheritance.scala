package fixtures

import eu.monniot.scala3mock.context.MockContext
import eu.monniot.scala3mock.macros.mock

trait ContextBoundInheritance[F[_]: Applicative]

trait ContextBoundInheritanceChild[F[_]: App2]
    extends ContextBoundInheritance[F]

trait App2[G[_]] extends Applicative[G]
object App2 {
  given App2[List] = new App2[List]:
    override def pure[A](a: A): List[A] = List(a)
}
