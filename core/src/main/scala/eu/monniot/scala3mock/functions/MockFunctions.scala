package eu.monniot.scala3mock.functions

import eu.monniot.scala3mock.context.MockContext
import eu.monniot.scala3mock.Default

object MockFunctions extends MockFunctions
trait MockFunctions {
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
}
