---
id: getting-started
title: Getting Started
---

This article describes how to get started with Scala3Mock. Because it is only an introduction, only the basics usage are described. For a comprehensive guide, see the [User Guide](user-guide/features.md).

## Install

To get started with SBT and ScalaTest, add the following dependencies to your **build.sbt**:
```scala
libraryDependencies += "eu.monniot" %% "scala3mock" % "@VERSION@" % Test
libraryDependencies += "eu.monniot" %% "scala3mock-scalatest" % "@VERSION@" % Test
```

While some testing framework integration exits, Scala3Mock at its core do not require one.
You can learn more about the various testing framework integration by going to to the dedicated page in the user guide. If there is no page, it means no special integration has been written yet (or it is not required). 

If you need more information on other build system, or the different versions of Scala3Mock, head on to the [Installation](user-guide/installation.md) part of the user guide.

## Basic Usage

> :info: The scala code you'll see in this example is being compiled and should work as is. Note that a snippet may depend on previous snippets.

First let's assume we have some functionality to test. We are going to be very imaginative and use a `Greeting` example. To simplify the implementation, it will have a `sayHello` function which take a name and a formatter, and print the formatted greeting.

```scala mdoc
object Greetings {
  sealed trait Formatter { def format(s: String): String }
  object English extends Formatter { def format(s: String) = s"Hello $s" }
  object French extends Formatter { def format(s: String) = s"Bonjour $s" }
  object Japanese extends Formatter { def format(s: String) = s"こんにちは $s" }

  def sayHello(name: String, formatter: Formatter): Unit =
    println(formatter.format(name))
}

// Let's import the content of our object for future snippets
import Greetings.*
```

With Scala3Mock, we can test the interaction between the `sayHello` function and a given `Formatter`. To be able to mock things, we need an implicit `MockContext`. The core library provides a `withExpectations` function that provide you with one.

For example, we can check that the name is actually being used and that our formatter is called only once.

To create a new mock, place the following contents into a Scala file within your project:

```scala mdoc
import main.withExpectations
import macros.{mock, when}

withExpectations() {
    val formatter = mock[Formatter]

    when(formatter.format)
      .expects("Mr Bond")
      .returns("Ah, Mr Bond. I've been expecting you")
      .once

    sayHello("Mr Bond", formatter)
}
```

Note that you do not have to specify the argument specifically but can instead accept any arguments to a mock with `AnyMatcher`. ScalaMock had a `*` alias for this. We are currently debating including that method in Scala3Mock, but you can do so yourself pretty easily as demonstrated below.

```scala mdoc
import matchers.MatchAny

def * = MatchAny()

withExpectations() {
    val formatter = mock[Formatter]

    when(formatter.format)
      .expects(*)
      .returns("Ah, Mr Bond. I've been expecting you")
      .once

    sayHello("Mr Bond", formatter)
}
```

Other basic available features that you may find useful are included below.

## Throwing an exception in a mock

```scala mdoc
withExpectations() {
  val brokenFormatter = mock[Formatter]

  when(brokenFormatter.format)
    .expects(*)
    .throwing(new NullPointerException)
    .anyNumberOfTimes

  try Greetings.sayHello("Erza", brokenFormatter)
  catch {
    case e: NullPointerException => println("expected")
  }
}
```

## Dynamic return value

```scala mdoc
withExpectations() {
  val australianFormat = mock[Formatter]

  when(australianFormat.format)
    .expects(*)
    .onCall { (s: String) => s"G'day $s" }
    .twice

  Greetings.sayHello("Wendy", australianFormat)
  Greetings.sayHello("Gray", australianFormat)
}
```

## Verifying arguments dynamically

```scala mdoc
import matchers.MatchPredicate.where

withExpectations() {
  val teamNatsu = Set("Natsu", "Lucy", "Happy", "Erza", "Gray", "Wendy", "Carla")
  val formatter = mock[Formatter]

  def assertTeamNatsu(s: String): Unit = {
    assert(teamNatsu.contains(s))
  }

  // 'where' verifies at the end of the test
  when(formatter.format)
    .expects(where { (s: String) => teamNatsu contains(s) })
    .onCall { (s: String) => s"Yo $s" }
    .twice

  Greetings.sayHello("Carla", formatter)
  Greetings.sayHello("Lucy", formatter)
}
```

## Further reading

Scala3Mock has some powerful features. The example below show some of them.

```scala mdoc:invisible
sealed trait Method
object Method {
  case object GET extends Method
  case object POST extends Method
}
sealed trait Http
object Http {
  case object NotFound extends Http
}
class HttpClient() {
  def sendRequest(m: Method, url: String, body: String): Http = ???
}
class Counter {
  def increment(i: Int): Int = ???
  def decrement: Int = ???
}
```

TODO: Need to support `onCall` on unary functions.

```scala mdoc:silent:fail
withExpectations(verifyAfterRun=false) {
  val httpClient = mock[HttpClient]
  val counterMock = mock[Counter]

  when(httpClient.sendRequest).expects(Method.GET, MatchAny(), MatchAny()).twice
  when(httpClient.sendRequest).expects(Method.POST, "http://scalamock.org", MatchAny()).noMoreThanOnce
  when(httpClient.sendRequest).expects(Method.POST, "http://example.com", MatchAny()).returning(Http.NotFound)
  when(counterMock.increment).expects(MatchAny()).onCall { (arg: Int) => arg + 1}
  when(counterMock.decrement).expects(MatchAny()).onCall { () => throw RuntimeException("here") }
}
```

Please read the [User Guide](user-guide/features.md) for more details.
