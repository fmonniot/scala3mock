package fixtures

// TODO Add a simple parameter to verify we can mock class with
// value parameter but no type parameter. I think I screwed up
// that case when adding type parameters support.
class TestClass {
  private def pm(x: Int) = x
  def m(x: Int, y: String) = (x, y)
}
