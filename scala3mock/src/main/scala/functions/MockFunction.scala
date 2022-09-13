package functions

import context.{Call, MockContext}
import handlers.*
import main.Default
import matchers.MockParameter

trait MockFunction:
  self: FakeFunction =>

  protected def onUnexpected(call: Call): Any = mockContext.reportUnexpectedCall(call)

class MockFunction0[R: Default](mockContext: MockContext, name: String)
  extends FakeFunction0[R](mockContext, name) with MockFunction:

  def expects(): CallHandler0[R] = mockContext.add(new CallHandler0[R](this))

  def expects(matcher: FunctionAdapter0[Boolean]): CallHandler0[R] = mockContext.add(new CallHandler0[R](this, matcher))

class MockFunction1[T1, R: Default](mockContext: MockContext, name: String)
  extends FakeFunction1[T1, R](mockContext, name) with MockFunction:

  // TODO Was taking MockParameter and using implicit conversion. What's the harm in taking the value directly? (non rhetoric question)
  def expects(v1: MockParameter[T1]): CallHandler1[T1, R] = mockContext.add(new CallHandler1[T1, R](this, v1))

  def expects(matcher: FunctionAdapter1[T1, Boolean]): CallHandler1[T1, R] = mockContext.add(new CallHandler1[T1, R](this, matcher))

class MockFunction2[T1, T2, R: Default](mockContext: MockContext, name: String)
  extends FakeFunction2[T1, T2, R](mockContext, name) with MockFunction:

  def expects(v1: MockParameter[T1], v2: MockParameter[T2]) = mockContext.add(new CallHandler2[T1, T2, R](this, v1, v2))
  def expects(matcher: FunctionAdapter2[T1, T2, Boolean]) = mockContext.add(new CallHandler2[T1, T2, R](this, matcher))

class MockFunction3[T1, T2, T3, R: Default](mockContext: MockContext, name: String)
  extends FakeFunction3[T1, T2, T3, R](mockContext, name) with MockFunction:

  def expects(v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3]) = mockContext.add(new CallHandler3[T1, T2, T3, R](this, v1, v2, v3))
  def expects(matcher: FunctionAdapter3[T1, T2, T3, Boolean]) = mockContext.add(new CallHandler3[T1, T2, T3, R](this, matcher))
