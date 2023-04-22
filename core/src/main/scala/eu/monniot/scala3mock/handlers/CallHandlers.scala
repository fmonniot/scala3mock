package eu.monniot.scala3mock.handlers

import eu.monniot.scala3mock.functions.*
import eu.monniot.scala3mock.context.Call
import eu.monniot.scala3mock.matchers.{ArgumentMatcher, MatcherBase}
import eu.monniot.scala3mock.main.Default

class CallHandler0[R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher):

  type Derived = CallHandler0[R]

  def this(target: FakeFunction) = this(target, new ArgumentMatcher(None))

  def onCall(handler: () => R): CallHandler0[R] =
    super.onCall(new FunctionAdapter0(handler))

class CallHandler1[T1, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher):

  type Derived = CallHandler1[T1, R]

  def this(target: FakeFunction, v1: T1 | MatcherBase) =
    this(target, new ArgumentMatcher(new Tuple1(v1)))

  def onCall(handler: (T1) => R): CallHandler1[T1, R] =
    super.onCall(new FunctionAdapter1(handler))

class CallHandler2[T1, T2, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher):

  type Derived = CallHandler2[T1, T2, R]

  def this(target: FakeFunction, v1: T1 | MatcherBase, v2: T2 | MatcherBase) =
    this(target, new ArgumentMatcher((v1, v2)))

  def onCall(handler: (T1, T2) => R): CallHandler2[T1, T2, R] =
    super.onCall(new FunctionAdapter2(handler))

class CallHandler3[T1, T2, T3, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler3[T1, T2, T3, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3)))

  def onCall(handler: (T1, T2, T3) => R): CallHandler3[T1, T2, T3, R] =
    super.onCall(new FunctionAdapter3(handler))
}

class CallHandler4[T1, T2, T3, T4, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler4[T1, T2, T3, T4, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3, v4)))

  def onCall(handler: (T1, T2, T3, T4) => R): CallHandler4[T1, T2, T3, T4, R] =
    super.onCall(new FunctionAdapter4(handler))
}

class CallHandler5[T1, T2, T3, T4, T5, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler5[T1, T2, T3, T4, T5, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5)))

  def onCall(
      handler: (T1, T2, T3, T4, T5) => R
  ): CallHandler5[T1, T2, T3, T4, T5, R] =
    super.onCall(new FunctionAdapter5(handler))
}

class CallHandler6[T1, T2, T3, T4, T5, T6, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler6[T1, T2, T3, T4, T5, T6, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6) => R
  ): CallHandler6[T1, T2, T3, T4, T5, T6, R] =
    super.onCall(new FunctionAdapter6(handler))
}

class CallHandler7[T1, T2, T3, T4, T5, T6, T7, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler7[T1, T2, T3, T4, T5, T6, T7, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7) => R
  ): CallHandler7[T1, T2, T3, T4, T5, T6, T7, R] =
    super.onCall(new FunctionAdapter7(handler))
}

class CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase,
      v8: T8 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8) => R
  ): CallHandler8[T1, T2, T3, T4, T5, T6, T7, T8, R] =
    super.onCall(new FunctionAdapter8(handler))
}

class CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]

  def this(
      target: FakeFunction,
      v1: T1 | MatcherBase,
      v2: T2 | MatcherBase,
      v3: T3 | MatcherBase,
      v4: T4 | MatcherBase,
      v5: T5 | MatcherBase,
      v6: T6 | MatcherBase,
      v7: T7 | MatcherBase,
      v8: T8 | MatcherBase,
      v9: T9 | MatcherBase
  ) = this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R
  ): CallHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] =
    super.onCall(new FunctionAdapter9(handler))
}

class CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]

  def this(
      target: FakeFunction,
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
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R
  ): CallHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] =
    super.onCall(new FunctionAdapter10(handler))
}
