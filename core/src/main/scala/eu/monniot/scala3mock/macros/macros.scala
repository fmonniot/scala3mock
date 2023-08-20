package eu.monniot.scala3mock.macros

import eu.monniot.scala3mock.context.{Mock, MockContext}
import eu.monniot.scala3mock.functions._
import eu.monniot.scala3mock.main.Default

trait Mocks:
  inline def mock[T](using MockContext): T = MockImpl[T]

  import scala.quoted.*

  // The Default constraint on the return type somehow help the compiler
  // not infer a type that's not the function type. For example without it
  // a mocked `Int => List[Int]` could be set up as `when(f).returns(List("a"))`
  // and the compiler would happily infer `MockFunction1[Int, Thing[? >: Int & String <: Int | String]]`
  // For some reasons with the type class constraint that is not the case anymore.

  inline def when[R: Default](inline f: () => R): MockFunction0[R] =
    (${ WhenImpl('f) })
  inline def when[T1, R: Default](inline f: T1 => R): MockFunction1[T1, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, R: Default](
      inline f: (T1, T2) => R
  ): MockFunction2[T1, T2, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, R: Default](
      inline f: (T1, T2, T3) => R
  ): MockFunction3[T1, T2, T3, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, R: Default](
      inline f: (T1, T2, T3, T4) => R
  ): MockFunction4[T1, T2, T3, T4, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, R: Default](
      inline f: (T1, T2, T3, T4, T5) => R
  ): MockFunction5[T1, T2, T3, T4, T5, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6) => R
  ): MockFunction6[T1, T2, T3, T4, T5, T6, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7) => R
  ): MockFunction7[T1, T2, T3, T4, T5, T6, T7, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8) => R
  ): MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R
  ): MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] = (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R
  ): MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] =
    (${ WhenImpl('f) })

object Mocks extends Mocks

export Mocks.*
