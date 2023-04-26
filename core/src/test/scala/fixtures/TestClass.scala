package fixtures

// Not the purest form, but who really care in a test ?
trait Applicative[F[_]] {
  def pure[A](a: A): F[A]
}

object Applicative {
  def apply[F[_]](using f: Applicative[F]) = f
  given Applicative[List] = new Applicative[List]:
    override def pure[A](a: A): List[A] = List(a)
}

// This test case is representative of a common implementation class in the pure
// functional programming world. I think.
class TestClass[F[_]: Applicative](dep: String)(dep2: Int)(using User) {
  private def pm(x: Int) = x
  
  def a(x: Int, y: String): (Int, String) = x -> y
  def b(x: Int): F[Int] = Applicative[F].pure(x)
}
