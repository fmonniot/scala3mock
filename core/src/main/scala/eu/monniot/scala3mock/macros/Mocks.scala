package eu.monniot.scala3mock.macros

import eu.monniot.scala3mock.context.{Mock, MockContext}
import eu.monniot.scala3mock.functions._
import eu.monniot.scala3mock.Default

trait Mocks:
  inline def mock[T](using MockContext): T = MockImpl[T]

  /** Like mock but enable internal debug log for our macros. Use when you have
    * found an issue and want to report it to the maintainers.
    */
  inline def mockWithDebuggingOutput[T](using MockContext): T =
    MockImpl.debug[T]

  import scala.quoted.*

  // The Default constraint on the return type somehow help the compiler
  // not infer a type that's not the function type. For example without it
  // a mocked `Int => List[Int]` could be set up as `when(f).returns(List("a"))`
  // and the compiler would happily infer `MockFunction1[Int, Thing[? >: Int & String <: Int | String]]`
  // For some reasons with the type class constraint that is not the case anymore.
  // format: off

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
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R
  ): MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R
  ): MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R
  ): MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R
  ): MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R
  ): MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R
  ): MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R
  ): MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R
  ): MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R
  ): MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R
  ): MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R
  ): MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R] =
    (${ WhenImpl('f) })
  inline def when[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Default](
      inline f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R
  ): MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R] =
    (${ WhenImpl('f) })

  // format: on

object Mocks extends Mocks

export Mocks.*
