package functions

import context.MockContext
import main.Default

trait MockFunctions {
  protected def mockFunction[R: Default](using ctx: MockContext) = new MockFunction0[R](ctx, "MockFunction0")
  protected def mockFunction[T1, R: Default](using ctx: MockContext) = new MockFunction1[T1, R](ctx, "MockFunction1")
  protected def mockFunction[T1, T2, R: Default](using ctx: MockContext) = new MockFunction2[T1, T2, R](ctx, "MockFunction2")
  protected def mockFunction[T1, T2, T3, R: Default](using ctx: MockContext) = new MockFunction3[T1, T2, T3, R](ctx, "MockFunction3")
}