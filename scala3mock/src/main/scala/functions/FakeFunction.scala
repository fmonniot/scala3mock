package functions

import context.{Call, MockContext}


// Base class. This hold the function state (mutable).
trait FakeFunction(protected val mockContext: MockContext, val name: String) {

  // [issue #25] we must always refer current mockContext vars because
  // they are updated during inititialize/resetExpectations!
  private def callLog = mockContext.callLog

  private def expectationContext = mockContext.expectationContext

  def handle(arguments: Product): Any = {
    if callLog != null then {
      val call = Call(this, arguments)
      callLog += call
      expectationContext.handle(call) getOrElse onUnexpected(call)
    } else {
      val msg = "Can't log call to mock object, have expectations been verified already?"
      throw new RuntimeException(msg)
    }
  }

  override def toString: String = scala.reflect.NameTransformer.decode(name)

  protected def onUnexpected(call: Call): Any
}


abstract class FakeFunction0[R](mockContext: MockContext, name: String)
  extends (() => R) with FakeFunction(mockContext, name) {

  def apply(): R = handle(None).asInstanceOf[R]
}

abstract class FakeFunction1[T1, R](mockContext: MockContext, name: String)
  extends (T1 => R) with FakeFunction(mockContext, name) {

  def apply(v1: T1): R = handle(Tuple1(v1)).asInstanceOf[R]
}

abstract class FakeFunction2[T1, T2, R](mockContext: MockContext, name: String)
  extends ((T1, T2) => R) with FakeFunction(mockContext, name) {

  def apply(v1: T1, v2: T2): R = handle((v1, v2)).asInstanceOf[R]
}

abstract class FakeFunction3[T1, T2, T3, R](mockContext: MockContext, name: String)
  extends ((T1, T2, T3) => R) with FakeFunction(mockContext, name) {

  def apply(v1: T1, v2: T2, v3: T3): R = handle((v1, v2, v3)).asInstanceOf[R]
}
