package eu.monniot.scala3mock.functions

import eu.monniot.scala3mock.context.{Call, MockContext}
import eu.monniot.scala3mock.handlers.*
import eu.monniot.scala3mock.matchers.MatcherBase
import eu.monniot.scala3mock.Default

trait MockFunction:
  self: FakeFunction =>

  protected def onUnexpected(call: Call): Any =
    mockContext.reportUnexpectedCall(call)

class MockFunction0[R: Default](mockContext: MockContext, name: String)
    extends FakeFunction0[R](mockContext, name)
    with MockFunction:

  def expects(): CallHandler0[R] = mockContext.add(new CallHandler0[R](this))

  def expects(matcher: FunctionAdapter0[Boolean]): CallHandler0[R] =
    mockContext.add(new CallHandler0[R](this, matcher))

class MockFunction1[T1, R: Default](mockContext: MockContext, name: String)
    extends FakeFunction1[T1, R](mockContext, name)
    with MockFunction:

  def expects(v1: T1 | MatcherBase): CallHandler1[T1, R] =
    mockContext.add(new CallHandler1(this, v1))

  def expects(matcher: FunctionAdapter1[T1, Boolean]): CallHandler1[T1, R] =
    mockContext.add(new CallHandler1(this, matcher))

class MockFunction2[T1, T2, R: Default](mockContext: MockContext, name: String)
    extends FakeFunction2[T1, T2, R](mockContext, name)
    with MockFunction:

  def expects(v1: T1 | MatcherBase, v2: T2 | MatcherBase) =
    mockContext.add(new CallHandler2(this, v1, v2))
  def expects(matcher: FunctionAdapter2[T1, T2, Boolean]) =
    mockContext.add(new CallHandler2(this, matcher))

class MockFunction3[T1, T2, T3, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction3[T1, T2, T3, R](mockContext, name)
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase
  ) = mockContext.add(new CallHandler3(this, v1, v2, v3))
  def expects(matcher: FunctionAdapter3[T1, T2, T3, Boolean]) =
    mockContext.add(new CallHandler3(this, matcher))

class MockFunction4[T1, T2, T3, T4, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction4[T1, T2, T3, T4, R](mockContext, name)
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase
  ) = mockContext.add(new CallHandler4(this, v1, v2, v3, v4))
  def expects(matcher: FunctionAdapter4[T1, T2, T3, T4, Boolean]) =
    mockContext.add(new CallHandler4(this, matcher))

class MockFunction5[T1, T2, T3, T4, T5, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction5[T1, T2, T3, T4, T5, R](mockContext, name)
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase
  ) = mockContext.add(new CallHandler5(this, v1, v2, v3, v4, v5))
  def expects(matcher: FunctionAdapter5[T1, T2, T3, T4, T5, Boolean]) =
    mockContext.add(new CallHandler5(this, matcher))

class MockFunction6[T1, T2, T3, T4, T5, T6, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction6[T1, T2, T3, T4, T5, T6, R](mockContext, name)
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase
  ) = mockContext.add(new CallHandler6(this, v1, v2, v3, v4, v5, v6))
  def expects(matcher: FunctionAdapter6[T1, T2, T3, T4, T5, T6, Boolean]) =
    mockContext.add(new CallHandler6(this, matcher))

class MockFunction7[T1, T2, T3, T4, T5, T6, T7, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction7[T1, T2, T3, T4, T5, T6, T7, R](mockContext, name)
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase
  ) = mockContext.add(new CallHandler7(this, v1, v2, v3, v4, v5, v6, v7))
  def expects(matcher: FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, Boolean]) =
    mockContext.add(new CallHandler7(this, matcher))

class MockFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](mockContext, name)
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase,
      v8: T8 | MatcherBase
  ) = mockContext.add(new CallHandler8(this, v1, v2, v3, v4, v5, v6, v7, v8))
  def expects(
      matcher: FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, Boolean]
  ) = mockContext.add(new CallHandler8(this, matcher))

class MockFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](
      mockContext,
      name
    )
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase,
      v8: T8 | MatcherBase,
      v9: T9 | MatcherBase
  ) =
    mockContext.add(new CallHandler9(this, v1, v2, v3, v4, v5, v6, v7, v8, v9))
  def expects(
      matcher: FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean]
  ) = mockContext.add(new CallHandler9(this, matcher))

class MockFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](
      mockContext,
      name
    )
    with MockFunction:

  def expects(
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase,
      v8: T8 | MatcherBase,
      v9: T9 | MatcherBase,
      v10: T10 | MatcherBase
  ) = mockContext.add(
    new CallHandler10(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)
  )
  def expects(
      matcher: FunctionAdapter10[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        Boolean
      ]
  ) = mockContext.add(new CallHandler10(this, matcher))
