package fixtures

class SpecializedClass2[@specialized T1, @specialized T2] extends SpecializedClass[T1] {
  def identity2(x: T1, y: T2) = (x,y)
}
