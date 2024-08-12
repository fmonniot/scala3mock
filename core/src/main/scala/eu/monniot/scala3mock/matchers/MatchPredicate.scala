package eu.monniot.scala3mock.matchers

import eu.monniot.scala3mock.functions.*

// format: off
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
  
  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => Boolean
  ): FunctionAdapter11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean] =
    FunctionAdapter11(matcher)
  
  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => Boolean
  ): FunctionAdapter12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean] =
    FunctionAdapter12(matcher)
  
  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => Boolean
  ): FunctionAdapter13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean] =
    FunctionAdapter13(matcher)
  
  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => Boolean
  ): FunctionAdapter14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean] =
    FunctionAdapter14(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => Boolean
  ): FunctionAdapter15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean] =
    FunctionAdapter15(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => Boolean
  ): FunctionAdapter16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean] =
    FunctionAdapter16(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => Boolean
  ): FunctionAdapter17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean] =
    FunctionAdapter17(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => Boolean
  ): FunctionAdapter18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean] =
    FunctionAdapter18(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => Boolean
  ): FunctionAdapter19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean] =
    FunctionAdapter19(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => Boolean
  ): FunctionAdapter20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean] =
    FunctionAdapter20(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => Boolean
  ): FunctionAdapter21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean] =
    FunctionAdapter21(matcher)

  def where[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](
      matcher: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => Boolean
  ): FunctionAdapter22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean] =
    FunctionAdapter22(matcher)
// format: on
object MatchPredicate extends MatchPredicate
