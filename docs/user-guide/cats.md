---
title: Cats Integration
---

Scala3Mock comes with a Cats integration that provides a `withExpectations` function that work with a `MonadError[?, Throwable]` instead of the simple value that the default library provide. This is pretty useful if your tests are defined in term of monads (Cats-Effect's `IO`, Scala's `Future` via alleycats, etc…).

You'll need to add a new dependency to your **build.sbt**:
```scala
libraryDependencies += "eu.monniot" %% "scala3mock-cats" % "@VERSION@" % Test
```

Once added, simply replace the `ScalaMocks` import with the Cats one:

```diff
- import eu.monniot.scala3mock.ScalaMocks.*
+ import eu.monniot.scala3mock.cats.ScalaMocks.*
```

You can then use the library like usual, with the value passed to `withExpectations` being a monad instead of any values.

```scala mdoc
import eu.monniot.scala3mock.cats.ScalaMocks.*
import cats.effect.SyncIO

val fa = withExpectations() {
    val fn = mockFunction[Int, String]
    fn.expects(*).returns("Hello reader")

    SyncIO(fn(2))
}

fa.unsafeRunSync()
```
