package eu.monniot.scala3mock.matchers

// The dummy argument (eugh!) is necessary to avoid:
//
// [error] double definition:
// [error] constructor MockParameter:(v: T)org.scalamock.matchers.MockParameter[T] and
// [error] constructor MockParameter:(value: AnyRef)org.scalamock.matchers.MockParameter[T]
// [error] have same type after erasure: (v: java.lang.Object)org.scalamock.matchers.MockParameter

class MockParameter[T] protected (val value: T | MatcherBase, dummy: Boolean = false):

  def this(v: T) = this(v, dummy = false)
  def this(v: MatcherBase) = this(v, dummy = false)

  override def hashCode() = value.##

  override def equals(that: Any) = value equals that

  override def toString = value.toString

object MockParameter extends MockParameterImplicitL1:
  given matchAny[T]: Conversion[MatcherBase, MockParameter[T]] = t => new MockParameter(t)

// We need this given instance to be less specific than the MatcherBase one. We
// do so by putting it at a greater inheritance level.
private class MockParameterImplicitL1:
  given specificValue[T]: Conversion[T, MockParameter[T]] = t => new MockParameter(t)
