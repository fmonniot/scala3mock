---
title: Advanced Topics
---

```scala mdoc:invisible
// Make shift mock context so that we can call `mock` without constraint.
// Don't do that in your test cases folks :)
given eu.monniot.scala3mock.context.MockContext = 
  new eu.monniot.scala3mock.context.MockContext:
    override type ExpectationException = eu.monniot.scala3mock.main.TestExpectationEx

    override def newExpectationException(message: String, methodName: Option[String]): ExpectationException =
      eu.monniot.scala3mock.main.TestExpectationEx(message, methodName)

    override def toString() = s"MockContext()"
```

First let's have some global imports

```scala mdoc
import eu.monniot.scala3mock.macros.{mock, when}
import eu.monniot.scala3mock.matchers.MatchAny
import eu.monniot.scala3mock.functions.MockFunctions.mockFunction
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

At the moment the `when` macro isn't smart enough to recognize a curried function whereas the `mock` macro is. That result in the underlying mock correctly creating a `MockFunction2[Int, Double, String]` (two args) but the `when` macro exposes a `MockFunction1[Int, Double => String]` (one arg).


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

## Call count

By default, mocks expect exactly one call. Alternative constraints can be set with `repeat`:

```scala mdoc:nest
val mockedFunction = mockFunction[Int, Int]

mockedFunction.expects(42).returns(42).repeat(3 to 7)
mockedFunction.expects(3).repeat(10)
```

There are various aliases for common expectations:

```scala mdoc:nest
val mockedFunction1 = mockFunction[Int, String]
val mockedFunction2 = mockFunction[Int, String]
val mockedFunction3 = mockFunction[Int, String]
val mockedFunction4 = mockFunction[Int, String]
val mockedFunction5 = mockFunction[Int, String]

mockedFunction1.expects(1).returning("foo").once
mockedFunction2.expects(2).returning("foo").noMoreThanTwice
mockedFunction3.expects(3).returning("foo").repeated(3)
mockedFunction4.expects(4).returning("foo").repeat(1 to 2)
mockedFunction5.expects(5).returning("foo").repeat(2)
mockedFunction5.expects(6).returning("foo").never

mockedFunction1(1)

mockedFunction3(3)
mockedFunction3(3)
mockedFunction3(3)

mockedFunction4(4)
mockedFunction4(4)

mockedFunction5(5)
mockedFunction5(5)
```

For a complete list, see `handlers.CallHandler`.

## Exceptions

Instead of returning a value, mock can be instructed to throw an exception. This can be achieved either by throwing an exception in `onCall` or by using the `throws` method.

```scala mdoc:nest
trait Foo {
  def increment(a: Int): Int
}

val fooMock = mock[Foo]


// Using the throws or throwing function
when(fooMock.increment).expects(5).throws(new RuntimeException("message"))
when(fooMock.increment).expects(6).throwing(new RuntimeException("message"))

try {
    fooMock.increment(5)
    false
} catch {
    case _: RuntimeException => true
    case _ => false
}


// Throwing in an onCall definition

when(fooMock.increment).expects(MatchAny()).onCall { (i) =>
  if(i==0) throw new RuntimeException("i == 0") 
  else i + 1
}

try {
    fooMock.increment(0)
    false
} catch {
    case _: RuntimeException => true
    case _ => false
}
```

## Argument Capture

ScalaMock support capturing argument when using mocks. This allow the usage of `MatchAny` while asserting the argument after the fact.

Scala3Mock doesn't current support this feature, although nothing in the library actively prevent its inclusion. Contribution welcome.

> :warn: TODO Insert issue number once created.

## Mocking 0-parameter functions

Mocking methods which have zero parameters or are parameter-less are a bit different. The `when` macro take a reference to the function without applying it. In the case of parameterless functions, such reference is indiguishable from applying it. Zero-parameters function could theoritically be seen as different, but that could lead to confusion (and indeed, it was legal in Scala 2 and turned out to be confusing).

To go around this, you can simply wrap the method into a `() => mock.method` function:

```scala mdoc
trait Test {
  def parameterless: Int
  def zeroParameter(): Int
}

val m = mock[Test]

when(() => m.zeroParameter()).expects().returns(1)
when(() => m.parameterless).expects().returns(1)
```
