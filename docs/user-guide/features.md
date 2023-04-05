---
title: Features
---

This is an overview of Scala3Mock features. Some section in this page have a dedicated page as well. Those pages will be indicated when present.

Do note that although the authors tried to be thorough when writing this page, some features might have slipped through the cracks. All supported use cases are covered by a test in the [core/features] and [core/mock] packages.

> TODO Fix link once I know the full URL they should point to.


Scala3Mock provides fully type-safe support for almost all Scala features. 

This includes:

- mocking classes, traits and case classes
- mocking functions and operators
- mocking type parametrised and overloaded methods
- support for type constraints
- support for repeated parameters and named parameters
- mocking Java classes and interfaces

Some notable features not supported:

- implicit method parameters (lack of compiler support)

Before going over the various features, let's define some types and import we can use along the way.

```scala mdoc
trait Heater {
    def isReady: Boolean
}

class CoffeeMachine(heater: Heater) {
    def powerOn(): Unit = ()
    def powerOff(): Unit = ()

    def isOn: Boolean = ???
}

case class Player(name: String, country: String)
case class Message(content: String)

abstract class Database {
    def getPlayerByName(name: String): Player
    def sendMessage(player: Player, message: Message): Unit

    def someMethod(s: String, d: Int): Int
    def otherMethod(s: String, d: Float): Int

    def increment(i: Int): Int
}

// And import the scala3mock content.
import macros.{mock, when}
import matchers.MatchAny
```

```scala mdoc:invisible
// Make shift mock context so that we can call `mock` without constraint.
// Don't do that in your test cases folks :)
given context.MockContext = 
  new context.MockContext:
    override type ExpectationException = main.TestExpectationEx

    override def newExpectationException(message: String, methodName: Option[String]): ExpectationException =
      main.TestExpectationEx(message, methodName)

    override def toString() = s"MockContext(callLog = $callLog)"

    // Initialize the context with one handler. That's fine here because we won't
    // be checking the assertion, only demonstrating how to set them up (which require
    // a handler to be present)
    val initialHandlers = handlers.UnorderedHandlers()
    callLog = scala.collection.mutable.ListBuffer[context.Call]()
    expectationContext = initialHandlers
    currentExpectationContext = initialHandlers
```

## Mocking

Scala3Mock can create mock out of classes (abstract or not, with parameters or not) and traits.

```scala mdoc
val heaterMock  = mock[Heater]
//val machineMock = mock[CoffeeMachine]
val dbMock      = mock[Database]
```

## Argument matching

```scala mdoc
import matchers.{MatchAny, MatchEpsilon, MatchPredicate}
// expect someMethod("foo", 42) to be called
when(dbMock.someMethod).expects("foo", 42)  

// expect someMethod("foo", x) to be called for some integer x
when(dbMock.someMethod).expects("foo", MatchAny())

// expect someMethod("foo", x) to be called for some float x that is close to 42.0
when(dbMock.otherMethod).expects("foo", MatchEpsilon(42.0))

// expect sendMessage(receiver, message) for some receiver with name starting with "A"
when(dbMock.sendMessage).expects(MatchPredicate.where { (receiver: Player, message: Message) => 
    receiver.name.startsWith("A")
}) 
```

## Call Ordering

Mock ordering is not currently supported in Scala3Mock. If you wish it to be, please add a thumb up to [issue#000].

> TODO Add correct issue number/link when I have one.

```scala mdoc:fail:invisible
// Keeping the snippet here just in case I end up implementing ordering

// expect that machine is turned on before turning it off
inSequence {
  (machineMock.turnOn _).expects()
  (machineMock.turnOff _).expects()
}

// players can be fetched in any order
inAnyOrder {
  (databaseMock.getPlayerByName _).expects("Hans")
  (databaseMock.getPlayerByName _).expects("Boris")
}
```

## Call count

```scala mdoc:silent
// expect message to be sent twice
when(dbMock.sendMessage).expects(MatchAny(), MatchAny()).twice

// expect message to be sent any number of times
when(dbMock.sendMessage).expects(MatchAny(), MatchAny()).anyNumberOfTimes

// expect message to be sent no more than twice
when(dbMock.sendMessage).expects(MatchAny(), MatchAny()).noMoreThanTwice
```

## Returning values

```scala mdoc:silent
when(dbMock.getPlayerByName).expects("Hans").returning(Player(name="Hans", country="Germany"))
when(dbMock.getPlayerByName).expects("Boris").returning(Player(name="Hans", country="Russia"))
```

## Throwing exceptions

```scala mdoc:silent
when(dbMock.getPlayerByName).expects("George").throwing(new NoSuchElementException)
```

## Call Handlers

If your mock needs to do more complex calculation, `onCall` will let you do exactly that:

```scala mdoc:silent
when(dbMock.increment).expects(MatchAny()).onCall {
    case 0 => throw RuntimeException("0 will throw")
    case arg => arg + 1
}.twice

// compute returned value
assert(dbMock.increment(100) == 101)

// throw computed exception
try dbMock.increment(0)
catch e => println(s"caught $e")
```

## Unsupported feature

At the time of writing, the Scala3 macro system doesn't support creating method with the `using`/`implicit` modifier. As a result, trying to mock any classes with a method using such modifier will fail. The example below show what error message you should expect when trying to do so. Hopefully that will save you some debugging time.

```scala mdoc:fail

abstract class TestClass {
    def method(using String): String
}

val testClass = mock[TestClass]
```
