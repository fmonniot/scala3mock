package eu.monniot.scala3mock.functions

import eu.monniot.scala3mock.context.{Call, MockContext}
import eu.monniot.scala3mock.handlers.*
import eu.monniot.scala3mock.matchers.MatcherBase
import eu.monniot.scala3mock.Default

trait MockFunction:
  self: FakeFunction =>

  protected def onUnexpected(call: Call): Any =
    mockContext.reportUnexpectedCall(call)

// format: off
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

class MockFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase
  ) = mockContext.add(
    new CallHandler11(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)
  )
  def expects(
      matcher: FunctionAdapter11[
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
        T11,
        Boolean
      ]
  ) = mockContext.add(new CallHandler11(this, matcher))

class MockFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase
  ) = mockContext.add(
    new CallHandler12(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)
  )
  def expects(
      matcher: FunctionAdapter12[
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
        T11,
        T12,
        Boolean
      ]
  ) = mockContext.add(new CallHandler12(this, matcher))

class MockFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase
  ) = mockContext.add(
    new CallHandler13(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)
  )
  def expects(
      matcher: FunctionAdapter13[
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
        T11,
        T12,
        T13,
        Boolean
      ]
  ) = mockContext.add(new CallHandler13(this, matcher))

class MockFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase
  ) = mockContext.add(
    new CallHandler14(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)
  )
  def expects(
      matcher: FunctionAdapter14[
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
        T11,
        T12,
        T13,
        T14,
        Boolean
      ]
  ) = mockContext.add(new CallHandler14(this, matcher))

class MockFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase
  ) = mockContext.add(
    new CallHandler15(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)
  )
  def expects(
      matcher: FunctionAdapter15[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        Boolean
      ]
  ) = mockContext.add(new CallHandler15(this, matcher))

class MockFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase
  ) = mockContext.add(
    new CallHandler16(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)
  )
  def expects(
      matcher: FunctionAdapter16[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        Boolean
      ]
  ) = mockContext.add(new CallHandler16(this, matcher))

class MockFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase
  ) = mockContext.add(
    new CallHandler17(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)
  )
  def expects(
      matcher: FunctionAdapter17[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        Boolean
      ]
  ) = mockContext.add(new CallHandler17(this, matcher))

class MockFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
      v18: T18 | MatcherBase
  ) = mockContext.add(
    new CallHandler18(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)
  )
  def expects(
      matcher: FunctionAdapter18[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        Boolean
      ]
  ) = mockContext.add(new CallHandler18(this, matcher))

class MockFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
      v18: T18 | MatcherBase,
      v19: T19 | MatcherBase
  ) = mockContext.add(
    new CallHandler19(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)
  )
  def expects(
      matcher: FunctionAdapter19[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        Boolean
      ]
  ) = mockContext.add(new CallHandler19(this, matcher))

class MockFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
      v18: T18 | MatcherBase,
      v19: T19 | MatcherBase,
      v20: T20 | MatcherBase
  ) = mockContext.add(
    new CallHandler20(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)
  )
  def expects(
      matcher: FunctionAdapter20[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        Boolean
      ]
  ) = mockContext.add(new CallHandler20(this, matcher))

class MockFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
      v18: T18 | MatcherBase,
      v19: T19 | MatcherBase,
      v20: T20 | MatcherBase,
      v21: T21 | MatcherBase
  ) = mockContext.add(
    new CallHandler21(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)
  )
  def expects(
      matcher: FunctionAdapter21[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        Boolean
      ]
  ) = mockContext.add(new CallHandler21(this, matcher))

class MockFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Default](
    mockContext: MockContext,
    name: String
) extends FakeFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](
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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
      v18: T18 | MatcherBase,
      v19: T19 | MatcherBase,
      v20: T20 | MatcherBase,
      v21: T21 | MatcherBase,
      v22: T22 | MatcherBase
  ) = mockContext.add(
    new CallHandler22(this, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)
  )
  def expects(
      matcher: FunctionAdapter22[
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
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        Boolean
      ]
  ) = mockContext.add(new CallHandler22(this, matcher))

  // format: off