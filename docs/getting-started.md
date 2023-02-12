---
id: getting-started
title: Getting Started
---

## Install

Add the following to your **build.sbt**:
```scala
libraryDependencies += "eu.monniot" %% "scala3mock" % "@VERSION@" % Test
```

## Usage

To create a new mock, place the following contents into a Scala file within your project:

```scala mdoc
import main.withExpectations
import functions.MockFunctions
import main.TestExpectationEx
import matchers.MatchAny
import macros.{mock, when}

trait TestTrait {
    def method(n: Int): String
}

withExpectations() {
    val m = mock[TestTrait]

    when(m.method).expects(42).returns("ok")
    assert(m.method(42) == "ok")
}
```

## Test Framework integration

### ScalaTest

TBD

### munit

TBD
