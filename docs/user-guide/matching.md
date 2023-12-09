---
title: Argument matching
---

Scala3Mock support three types of matching mocked functions arguments:

- any matching, also called wildcards
- epsilon matching,
- predicate matching


In this page we will make use of a few common declarations, so let's get them out of the way

```scala mdoc:invisible
// Make shift mock context so that we can call `mock` without constraint.
// Don't do that in your test cases folks :)
given eu.monniot.scala3mock.context.MockContext = 
  new eu.monniot.scala3mock.context.MockContext:
    override type ExpectationException = eu.monniot.scala3mock.MockExpectationFailed

    override def newExpectationException(message: String, methodName: Option[String]): ExpectationException =
      eu.monniot.scala3mock.MockExpectationFailed(message, methodName)

    override def toString() = s"MockContext()"
```

```scala mdoc
import eu.monniot.scala3mock.ScalaMocks.*

val mockedFunction = mockFunction[String, Any, Unit]
```

## Any matching

Any matching are defined using the `MatchAny` class, or more commonly its `*` alias. For example:

```scala mdoc
mockedFunction.expects("", *).anyNumberOfTimes
```

will match any of the following:

```scala mdoc
mockedFunction("", "")
mockedFunction("", 1.0)
mockedFunction("", null)
mockedFunction("", Object())
```

## Predicate matching

More complicated argument matching can be implemented by using `where` to provide your own logic.

```scala
trait MatchPredicate:
  def where[Arg1](predicate: (Arg1) => Boolean)
  def where[Arg1, Arg2](predicate: (Arg1, Arg2) => Boolean)
```

### Example 1

In this example we will use the following `PlayerLeaderboard` interface.

```scala mdoc
case class Player(id: Long, name: String, emailAddress: String, country: String)

trait PlayerLeaderBoard {
    def addPointsForPlayer(player: Player, points: Int): Unit
}

val leaderBoardMock = mock[PlayerLeaderBoard]
```

Now imagine that we want to put an expectation that `addPointsForPlayer` is called with:

- `points` equal to 100, and
- `player` can have any `name`, any `email`, any `country`, as long as its `id` is 789.

Achieving that can be done using the `where` predicate:

```scala mdoc
when(leaderBoardMock.addPointsForPlayer).expects(where {
  (player: Player, points: Int) => player.id == 789 && points == 100
}) 
```

### Example 2

This second example is simpler but shows the power of using arbitrary predicate. Here the mock will only return if the first argument is smaller than the second one.

```scala mdoc
val mockedFunction2 = mockFunction[Double, Double, Unit] // (Double, Double) => Unit
mockedFunction2.expects(where { _ < _ }) // expects that arg1 < arg2 
```


## Epsilon matching

Epsilon matching is useful when dealing with floating point values. An epsilon match is specified with the `MatchEpsilon` class, or more commonly its `~` alias:

```scala mdoc
val mockedFunction3 = mockFunction[Double, Unit] // (Double, Double) => Unit

mockedFunction3.expects(~42.0).anyNumberOfTimes
```

will match:

```scala mdoc
mockedFunction3(42.0)
mockedFunction3(42.0001)
mockedFunction3(41.9999)
```

but will not match:

```scala mdoc:crash
mockedFunction3(43.0)
mockedFunction3(42.1)
```
