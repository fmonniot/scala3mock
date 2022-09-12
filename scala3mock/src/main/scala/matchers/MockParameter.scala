package matchers

// The dummy argument (eugh!) is necessary to avoid:
//
// [error] double definition:
// [error] constructor MockParameter:(v: T)org.scalamock.matchers.MockParameter[T] and
// [error] constructor MockParameter:(value: AnyRef)org.scalamock.matchers.MockParameter[T]
// [error] have same type after erasure: (v: java.lang.Object)org.scalamock.matchers.MockParameter

class MockParameter[T] protected (val value: AnyRef, dummy: Boolean = false):

  def this(v: T) = this(v.asInstanceOf[AnyRef], dummy = false)
  def this(v: MatcherBase) = this(v.asInstanceOf[AnyRef], dummy = false)

  override def hashCode() = value.##

  override def equals(that: Any) = value equals that

  override def toString = value.toString
