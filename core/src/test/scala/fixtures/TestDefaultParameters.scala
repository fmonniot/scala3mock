package fixtures

trait TestDefaultParameters {
  def foo(bar: Int = 9): String
  def foo2(b: Int = 1, c: Long): String

  def defaultAfterRegular(a: Int, b: Int = 0): String
  def multiParamList(a: Int, b: Int = 0)(c: Long, d: Long = 0): String

  def withTypeParam[A, B](a: A, b: B)(c: Long, d: Long = 0): String
}
