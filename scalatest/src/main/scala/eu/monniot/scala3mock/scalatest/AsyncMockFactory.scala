package eu.monniot.scala3mock.scalatest

import context.{Mock, MockContext}
import macros.{MockImpl, WhenImpl}
import functions._

trait AsyncMockFactory {
    given MockContext = ???

    inline def mock[T](using MockContext): T = MockImpl[T]

    inline def when[R](inline f: () => R): MockFunction0[R] = WhenImpl(f)
    inline def when[T1, R](inline f: T1 => R): MockFunction1[T1, R] = WhenImpl(f)
}
