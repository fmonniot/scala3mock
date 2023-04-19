package eu.monniot.scala3mock.functions

import eu.monniot.scala3mock.context.MockContext
import eu.monniot.scala3mock.main.Default

object MockFunctions extends MockFunctions
trait MockFunctions {
  def mockFunction[R: Default](using ctx: MockContext) = new MockFunction0[R](ctx, "MockFunction0")
  def mockFunction[T1, R: Default](using ctx: MockContext) = new MockFunction1[T1, R](ctx, "MockFunction1")
  def mockFunction[T1, T2, R: Default](using ctx: MockContext) = new MockFunction2[T1, T2, R](ctx, "MockFunction2")
  def mockFunction[T1, T2, T3, R: Default](using ctx: MockContext) = new MockFunction3[T1, T2, T3, R](ctx, "MockFunction3")
}
