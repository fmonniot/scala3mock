package matchers

class MatchAny extends MatcherBase:

  override def canEqual(that: Any) = true

  override def equals(that: Any) = true

  override def toString = "*"
