package eu.monniot.scala3mock.matchers

import eu.monniot.scala3mock.functions.*

trait MatchPredicate:
  def where[T1](matcher: T1 => Boolean): FunctionAdapter1[T1, Boolean] =
    FunctionAdapter1(matcher)
  def where[T1, T2](
      matcher: (T1, T2) => Boolean
  ): FunctionAdapter2[T1, T2, Boolean] = FunctionAdapter2(matcher)
  def where[T1, T2, T3](
      matcher: (T1, T2, T3) => Boolean
  ): FunctionAdapter3[T1, T2, T3, Boolean] = FunctionAdapter3(matcher)
  def where[T1, T2, T3, T4](
      matcher: (T1, T2, T3, T4) => Boolean
  ): FunctionAdapter4[T1, T2, T3, T4, Boolean] = FunctionAdapter4(matcher)
  def where[T1, T2, T3, T4, T5](
      matcher: (T1, T2, T3, T4, T5) => Boolean
  ): FunctionAdapter5[T1, T2, T3, T4, T5, Boolean] = FunctionAdapter5(matcher)
  def where[T1, T2, T3, T4, T5, T6](
      matcher: (T1, T2, T3, T4, T5, T6) => Boolean
  ): FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean] = FunctionAdapter6(
    matcher
  )
  def where[T1, T2, T3, T4, T5, T6, T7](
      matcher: (T1, T2, T3, T4, T5, T6, T7) => Boolean
  ): FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean] = FunctionAdapter7(
    matcher
  )
  def where[T1, T2, T3, T4, T5, T6, T7, T8](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8) => Boolean
  ): FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean] =
    FunctionAdapter8(matcher)
  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Boolean
  ): FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean] =
    FunctionAdapter9(matcher)
  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Boolean
  ): FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean] =
    FunctionAdapter10(matcher)

object MatchPredicate extends MatchPredicate
