package eu.monniot.scala3mock.handlers

import eu.monniot.scala3mock.functions.*
import eu.monniot.scala3mock.context.Call
import eu.monniot.scala3mock.matchers.{ArgumentMatcher, MatcherBase}
import eu.monniot.scala3mock.Default

// format: off
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

class CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => R
  ): CallHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] =
    super.onCall(new FunctionAdapter11(handler))
}

class CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => R
  ): CallHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] =
    super.onCall(new FunctionAdapter12(handler))
}

class CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => R
  ): CallHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R] =
    super.onCall(new FunctionAdapter13(handler))
}

class CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => R
  ): CallHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R] =
    super.onCall(new FunctionAdapter14(handler))
}

class CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => R
  ): CallHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R] =
    super.onCall(new FunctionAdapter15(handler))
}

class CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => R
  ): CallHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R] =
    super.onCall(new FunctionAdapter16(handler))
}

class CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => R
  ): CallHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R] =
    super.onCall(new FunctionAdapter17(handler))
}

class CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R]

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
      v10: T10 | MatcherBase,
      v11: T11 | MatcherBase,
      v12: T12 | MatcherBase,
      v13: T13 | MatcherBase,
      v14: T14 | MatcherBase,
      v15: T15 | MatcherBase,
      v16: T16 | MatcherBase,
      v17: T17 | MatcherBase,
      v18: T18 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => R
  ): CallHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R] =
    super.onCall(new FunctionAdapter18(handler))
}

class CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R]

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
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => R
  ): CallHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R] =
    super.onCall(new FunctionAdapter19(handler))
}

class CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R]

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
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => R
  ): CallHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R] =
    super.onCall(new FunctionAdapter20(handler))
}

class CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R]

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
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => R
  ): CallHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R] =
    super.onCall(new FunctionAdapter21(handler))
}

class CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R: Default](
    target: FakeFunction,
    argumentMatcher: Product => Boolean
) extends CallHandler[R](target, argumentMatcher) {

  type Derived = CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R]

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
      v22: T22 | MatcherBase,
  ) =
    this(target, new ArgumentMatcher((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)))

  def onCall(
      handler: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => R
  ): CallHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R] =
    super.onCall(new FunctionAdapter22(handler))
}

// format: on