---
title: Advanced Topics
---

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

First let's have some global imports

```scala mdoc
import macros.{mock, when}
import matchers.MatchAny
import functions.MockFunctions.mockFunction
```

## Mocking overloaded, curried and polymorphic methods
Overloaded, curried and polymorphic methods can be mocked by specifying either argument types or type parameters.

### Overloaded methods

```scala mdoc:nest
trait Foo {
  def overloaded(x: Int): String
  def overloaded(x: String): String
  def overloaded[T](x: T): String
}

val fooMock = mock[Foo]

when(fooMock.overloaded(_: Int)).expects(10)
when(fooMock.overloaded(_: String)).expects("foo")
when(fooMock.overloaded[Double]).expects(1.23)
```

### Polymorphic methods

```scala mdoc:nest
trait Foo {
  def polymorphic[T](x: List[T]): String
}

val fooMock = mock[Foo]

when(fooMock.polymorphic(_: List[Int])).expects(List(1, 2, 3))
```


### Curried methods

```scala mdoc:nest
trait Foo {
  def curried(x: Int)(y: Double): String
}

val fooMock = mock[Foo]

when(fooMock.curried(_: Int)(_: Double)).expects(10, 1.23)
```

The project have an action item to make the following statement compile:

```scala mdoc:nest:crash
trait Foo {
  def curried(x: Int)(y: Double): String
}

val fooMock = mock[Foo]

when(fooMock.curried)
```

At the moment the `when` macro isn't smart enough to recognize a curried function whereas the `mock` macro is. That result in the underlying mock correctly creating a `MockFunction2` (two args) but the `when` macro exposes a `MockFunction1` (one arg).


## Methods with implicit parameters

> :warn: This is currently not possible in Scala 3, as the macro system doesn't let us create given parameter list in the mock class.

This case is very similar to curried methods. All you need to do is to help the Scala compiler know that `memcachedMock.get` should be converted to `MockFunction2`. For example:

```scala mdoc:nest:fail
class Codec()

trait Memcached {
  def get(key: String)(implicit codec: Codec): Option[Int]
}

val memcachedMock = mock[Memcached]

implicit val codec = new Codec
when(memcachedMock.get(_ : String)(_ : Codec)).expects("some_key", MatchAny()).returning(Some(123))
```



### Repeated parameters

Repeated parameters are represented as a Seq. For example, given:


```scala mdoc:nest
trait Foo {
  def takesRepeatedParameter(x: Int, ys: String*): Unit
}

val fooMock = mock[Foo]

when(fooMock.takesRepeatedParameter).expects(42, Seq("red", "green", "blue"))
```

## Returning values (onCall)

By default mocks and stubs return `null`. You can return predefined value using the `returning` method. When the returned value depends on function arguments, you can return the computed value (or throw a computed exception) with `onCall`. For example:


```scala mdoc:nest
trait Foo {
    def increment(a: Int): Int
}

val fooMock = mock[Foo]

when(fooMock.increment).expects(12).returning(13)
assert(fooMock.increment(12) == 13)

when(fooMock.increment).expects(MatchAny()).onCall { (arg: Int) => arg + 1}
assert(fooMock.increment(100) == 101)

when(fooMock.increment).expects(MatchAny()).onCall { arg => throw new RuntimeException("message") }
try {
    fooMock.increment(0)
    false
} catch {
    case _: RuntimeException => true
    case _ => false
}
```

```scala mdoc:nest
val mockIncrement = mockFunction[Int, Int]
mockIncrement.expects(MatchAny()).onCall { (arg: Int) => arg + 1 }
assert(mockIncrement(10) == 11)
```
