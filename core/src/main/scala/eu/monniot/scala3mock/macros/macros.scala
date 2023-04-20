package eu.monniot.scala3mock.macros

import eu.monniot.scala3mock.context.{Mock, MockContext}
import eu.monniot.scala3mock.functions._

trait Mocks:
  inline def mock[T](using MockContext): T = MockImpl[T]

  inline def when[R](inline f: () => R): MockFunction0[R] = WhenImpl(f)
  inline def when[T1, R](inline f: T1 => R): MockFunction1[T1, R] = WhenImpl(f)
  inline def when[T1, T2, R](
      inline f: (T1, T2) => R
  ): MockFunction2[T1, T2, R] = WhenImpl(f)
  inline def when[T1, T2, T3, R](
      inline f: (T1, T2, T3) => R
  ): MockFunction3[T1, T2, T3, R] = WhenImpl(f)
  inline def when[T1, T2, T3, T4, R](
      inline f: (T1, T2, T3, T4) => R
  ): MockFunction4[T1, T2, T3, T4, R] = WhenImpl(f)
  inline def apply[T1, T2, T3, T4, T5, R](
      inline f: (T1, T2, T3, T4, T5) => R
  ): MockFunction5[T1, T2, T3, T4, T5, R] = WhenImpl(f)
  inline def apply[T1, T2, T3, T4, T5, T6, R](
      inline f: (T1, T2, T3, T4, T5, T6) => R
  ): MockFunction6[T1, T2, T3, T4, T5, T6, R] = WhenImpl(f)
  inline def apply[T1, T2, T3, T4, T5, T6, T7, R](
      inline f: (T1, T2, T3, T4, T5, T6, T7) => R
  ): MockFunction7[T1, T2, T3, T4, T5, T6, T7, R] = WhenImpl(f)
  inline def apply[T1, T2, T3, T4, T5, T6, T7, T8, R](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8) => R
  ): MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R] = WhenImpl(f)
  inline def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R
  ): MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = WhenImpl(f)
  inline def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R
  ): MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] = WhenImpl(f)

object Mocks extends Mocks

export Mocks.*
