package eu.monniot.scala3mock.functions

import eu.monniot.scala3mock.context.{Call, MockContext}

// Base class. This hold the function state (mutable).
trait FakeFunction(protected val mockContext: MockContext, val name: String):

  // [issue #25] we must always refer current mockContext vars because
  // they are updated during inititialize/resetExpectations!
  private def callLog = mockContext.callLog

  private def expectationContext = mockContext.expectationContext

  def handle(arguments: Product): Any =
    if callLog != null then
      val call = Call(this, arguments)
      callLog += call
      expectationContext.handle(call) getOrElse onUnexpected(call)
    else
      val msg =
        "Can't log call to mock object, have expectations been verified already?"
      throw new RuntimeException(msg)

  override def toString: String = scala.reflect.NameTransformer.decode(name)

  protected def onUnexpected(call: Call): Any

abstract class FakeFunction0[R](mockContext: MockContext, name: String)
    extends (() => R)
    with FakeFunction(mockContext, name):

  def apply(): R = handle(None).asInstanceOf[R]

abstract class FakeFunction1[T1, R](mockContext: MockContext, name: String)
    extends (T1 => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1): R = handle(Tuple1(v1)).asInstanceOf[R]

abstract class FakeFunction2[T1, T2, R](mockContext: MockContext, name: String)
    extends ((T1, T2) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2): R = handle((v1, v2)).asInstanceOf[R]

abstract class FakeFunction3[T1, T2, T3, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3): R = handle((v1, v2, v3)).asInstanceOf[R]

abstract class FakeFunction4[T1, T2, T3, T4, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4): R =
    handle((v1, v2, v3, v4)).asInstanceOf[R]

abstract class FakeFunction5[T1, T2, T3, T4, T5, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5): R = handle(
    (v1, v2, v3, v4, v5)
  ).asInstanceOf[R]

abstract class FakeFunction6[T1, T2, T3, T4, T5, T6, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6): R = handle(
    (v1, v2, v3, v4, v5, v6)
  ).asInstanceOf[R]

abstract class FakeFunction7[T1, T2, T3, T4, T5, T6, T7, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7): R = handle(
    (v1, v2, v3, v4, v5, v6, v7)
  ).asInstanceOf[R]

abstract class FakeFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8) => R)
    with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8): R =
    handle((v1, v2, v3, v4, v5, v6, v7, v8)).asInstanceOf[R]

abstract class FakeFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9)).asInstanceOf[R]

abstract class FakeFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)).asInstanceOf[R]

abstract class FakeFunction11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)).asInstanceOf[R]

abstract class FakeFunction12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)).asInstanceOf[R]

abstract class FakeFunction13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)).asInstanceOf[R]

abstract class FakeFunction14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)).asInstanceOf[R]

abstract class FakeFunction15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)).asInstanceOf[R]

abstract class FakeFunction16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)).asInstanceOf[R]

abstract class FakeFunction17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
      v17: T17,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)).asInstanceOf[R]

abstract class FakeFunction18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
      v17: T17,
      v18: T18,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)).asInstanceOf[R]

abstract class FakeFunction19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
      v17: T17,
      v18: T18,
      v19: T19,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)).asInstanceOf[R]

abstract class FakeFunction20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
      v17: T17,
      v18: T18,
      v19: T19,
      v20: T20,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)).asInstanceOf[R]

abstract class FakeFunction21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
      v17: T17,
      v18: T18,
      v19: T19,
      v20: T20,
      v21: T21,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)).asInstanceOf[R]

abstract class FakeFunction22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](
    mockContext: MockContext,
    name: String
) extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R)
    with FakeFunction(mockContext, name):

  def apply(
      v1: T1,
      v2: T2,
      v3: T3,
      v4: T4,
      v5: T5,
      v6: T6,
      v7: T7,
      v8: T8,
      v9: T9,
      v10: T10,
      v11: T11,
      v12: T12,
      v13: T13,
      v14: T14,
      v15: T15,
      v16: T16,
      v17: T17,
      v18: T18,
      v19: T19,
      v20: T20,
      v21: T21,
      v22: T22,
  ): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)).asInstanceOf[R]
