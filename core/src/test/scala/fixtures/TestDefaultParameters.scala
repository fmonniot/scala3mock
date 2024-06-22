package fixtures

trait TestDefaultParameters {
  def foo(bar: Int = 9): String
  def foo2(b: Int = 1, c: Long): String
}
