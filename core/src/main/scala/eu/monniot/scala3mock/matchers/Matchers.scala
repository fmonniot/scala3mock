package eu.monniot.scala3mock.matchers

object Matchers extends Matchers

trait Matchers extends MatchPredicate:

  def * = MatchAny()
  def ~(value: Double) = MatchEpsilon(value)
