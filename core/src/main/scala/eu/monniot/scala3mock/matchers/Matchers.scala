package eu.monniot.scala3mock.matchers

object Matchers extends Matchers

trait Matchers extends MatchPredicate:

  def * = MatchAny()

  extension (value: Double) def unary_~ = MatchEpsilon(value)
