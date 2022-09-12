package handlers

import functions.*
import context.Call
import matchers.{ArgumentMatcher, MockParameter}
import main.Default


class CallHandler0[R: Default](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher):

  type Derived = CallHandler0[R]

  def this(target: FakeFunction) = this(target, new ArgumentMatcher(None))

  def onCall(handler: () => R): CallHandler0[R] = super.onCall(new FunctionAdapter0(handler))

class CallHandler1[T1, R: Default](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher):

  type Derived = CallHandler1[T1, R]

  def this(target: FakeFunction, v1: MockParameter[T1]) = this(target, new ArgumentMatcher(new Tuple1(v1)))

  def onCall(handler: (T1) => R): CallHandler1[T1, R] = super.onCall(new FunctionAdapter1(handler))

class CallHandler2[T1, T2, R: Default](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher):

  type Derived = CallHandler2[T1, T2, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2]) = this(target, new ArgumentMatcher((v1, v2)))

  def onCall(handler: (T1, T2) => R): CallHandler2[T1, T2, R] = super.onCall(new FunctionAdapter2(handler))

class CallHandler3[T1, T2, T3, R: Default](target: FakeFunction, argumentMatcher: Product => Boolean) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler3[T1, T2, T3, R]

  def this(target: FakeFunction, v1: MockParameter[T1], v2: MockParameter[T2], v3: MockParameter[T3]) = this(target, new ArgumentMatcher((v1, v2, v3)))

  def onCall(handler: (T1, T2, T3) => R): CallHandler3[T1, T2, T3, R] = super.onCall(new FunctionAdapter3(handler))
}