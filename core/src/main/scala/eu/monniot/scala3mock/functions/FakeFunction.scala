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
      val msg = "Can't log call to mock object, have expectations been verified already?"
      throw new RuntimeException(msg)

  override def toString: String = scala.reflect.NameTransformer.decode(name)

  protected def onUnexpected(call: Call): Any


abstract class FakeFunction0[R](mockContext: MockContext, name: String)
  extends (() => R) with FakeFunction(mockContext, name):

  def apply(): R = handle(None).asInstanceOf[R]

abstract class FakeFunction1[T1, R](mockContext: MockContext, name: String)
  extends (T1 => R) with FakeFunction(mockContext, name):

  def apply(v1: T1): R = handle(Tuple1(v1)).asInstanceOf[R]

abstract class FakeFunction2[T1, T2, R](mockContext: MockContext, name: String)
  extends ((T1, T2) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2): R = handle((v1, v2)).asInstanceOf[R]

abstract class FakeFunction3[T1, T2, T3, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3): R = handle((v1, v2, v3)).asInstanceOf[R]

abstract class FakeFunction4[T1, T2, T3, T4, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4): R = handle((v1, v2, v3, v4)).asInstanceOf[R]

abstract class FakeFunction5[T1, T2, T3, T4, T5, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4, T5) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5): R = handle((v1, v2, v3, v4, v5)).asInstanceOf[R]

abstract class FakeFunction6[T1, T2, T3, T4, T5, T6, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4, T5, T6) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6): R = handle((v1, v2, v3, v4, v5, v6)).asInstanceOf[R]

abstract class FakeFunction7[T1, T2, T3, T4, T5, T6, T7, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4, T5, T6, T7) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7): R = handle((v1, v2, v3, v4, v5, v6, v7)).asInstanceOf[R]

abstract class FakeFunction8[T1, T2, T3, T4, T5, T6, T7, T8, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4, T5, T6, T7, T8) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8): R = handle((v1, v2, v3, v4, v5, v6, v7, v8)).asInstanceOf[R]

abstract class FakeFunction9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9)).asInstanceOf[R]

abstract class FakeFunction10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R) with FakeFunction(mockContext, name):

  def apply(v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6, v7: T7, v8: T8, v9: T9, v10: T10): R = handle((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)).asInstanceOf[R]

