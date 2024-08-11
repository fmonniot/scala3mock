package eu.monniot.scala3mock.functions

import eu.monniot.scala3mock.context.MockContext
import eu.monniot.scala3mock.Default

object MockFunctions extends MockFunctions
trait MockFunctions {
  // format: off
  def mockFunction[R: Default](using ctx: MockContext) =
    new MockFunction0[R](ctx, "MockFunction0")
  def mockFunction[T1, R: Default](using ctx: MockContext) =
    new MockFunction1[T1, R](ctx, "MockFunction1")
  def mockFunction[T1, T2, R: Default](using ctx: MockContext) =
    new MockFunction2[T1, T2, R](ctx, "MockFunction2")
  def mockFunction[T1, T2, T3, R: Default](using ctx: MockContext) =
    new MockFunction3[T1, T2, T3, R](ctx, "MockFunction3")
  def mockFunction[T1, T2, T3, T4, R: Default](using ctx: MockContext) =
    new MockFunction4[T1, T2, T3, T4, R](ctx, "MockFunction4")
  def mockFunction[T1, T2, T3, T4, T5, R: Default](using ctx: MockContext) =
    new MockFunction5[T1, T2, T3, T4, T5, R](ctx, "MockFunction5")
  def mockFunction[T1, T2, T3, T4, T5, T6, R: Default](using ctx: MockContext) =
    new MockFunction6[T1, T2, T3, T4, T5, T6, R](ctx, "MockFunction6")
  def mockFunction[T1, T2, T3, T4, T5, T6, T7, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction7[T1, T2, T3, T4, T5, T6, T7, R](ctx, "MockFunction7")
  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](ctx, "MockFunction8")
  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](
      ctx,
      "MockFunction9"
    )
  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](
      ctx,
      "MockFunction10"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](
      ctx,
      "MockFunction11"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](
      ctx,
      "MockFunction12"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](
      ctx,
      "MockFunction13"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](
      ctx,
      "MockFunction14"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](
      ctx,
      "MockFunction15"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](
      ctx,
      "MockFunction16"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](
      ctx,
      "MockFunction17"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](
      ctx,
      "MockFunction18"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](
      ctx,
      "MockFunction19"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](
      ctx,
      "MockFunction20"
    )

  def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](
      ctx,
      "MockFunction21"
    )

   def mockFunction[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Default](using
      ctx: MockContext
  ) =
    new MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](
      ctx,
      "MockFunction22"
    )

  // format: on
}
