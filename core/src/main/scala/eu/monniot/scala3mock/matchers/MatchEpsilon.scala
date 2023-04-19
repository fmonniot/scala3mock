package eu.monniot.scala3mock.matchers

import scala.math.abs

/** Matcher that matches all numbers that are close to a given value */
class MatchEpsilon(value: Double) extends MatcherBase:

  override def canEqual(that: Any) = that.isInstanceOf[Number]

  override def equals(that: Any) = that match {
    case n: Number => abs(value - n.doubleValue) < MatchEpsilon.epsilon
    case _ => false
  }

  override def toString = "~" + value


object MatchEpsilon:
  val epsilon = 0.001
