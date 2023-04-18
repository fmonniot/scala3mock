---
title: ScalaTest Integration
---

```scala mdoc:invisible
// This code block provide the classes required to compile the other snippet.
import scala.concurrent.Future

class WaterContainer {
    def isEmpty: Boolean = ???
    def isOverfull: Boolean = ???
}

case class Currency(id: String, valueToUSD: Double, change: Double)
class CurrencyDatabase() {
    def getCurrency(id: String): Currency = ???
}
class ExchangeRateListing(db: CurrencyDatabase) {
    def getExchangeRate(a: String, b: String): Future[Double] = ???
}

class Heater() {
    def isReady: Boolean = ???
}
class CoffeeMachine(a: WaterContainer, b: Heater) {
    def powerOn(): Unit = ()
    def powerOff(): Unit = ()

    def isOn: Boolean = ???
}

```

## Integration

To provide mocking support in ScalaTest, extends your suite with `eu.monniot.scala3mock.scalatest.MockFactory`:


```scala mdoc
import org.scalatest.flatspec.AnyFlatSpec
import eu.monniot.scala3mock.scalatest.MockFactory

class CoffeeMachineTest extends AnyFlatSpec with MockFactory {

  "CoffeeMachine" should "not turn on the heater when the water container is empty" in {
      val waterContainerMock = mock[WaterContainer]
      when(() => waterContainerMock.isEmpty).expects().returning(true)
      // ...
  }
}
```

When using the asynchronous version, please use `eu.monniot.scala3mock.scalatest.AsyncMockFactory` instead:

```scala mdoc
import org.scalatest.flatspec.AsyncFlatSpec
import eu.monniot.scala3mock.scalatest.AsyncMockFactory

class ExchangeRateListingTest extends AsyncFlatSpec with AsyncMockFactory {

  val eur = Currency(id = "EUR", valueToUSD = 1.0531, change = -0.0016)
  val gpb = Currency(id = "GPB", valueToUSD = 1.2280, change = -0.0012)
  val aud = Currency(id = "AUD", valueToUSD = 0.7656, change = -0.0024)

  "ExchangeRateListing" should "eventually return the exchange rate between passed Currencies when getExchangeRate is invoked" in {
      val currencyDatabaseMock = mock[CurrencyDatabase]
      
      when(currencyDatabaseMock.getCurrency).expects(eur.id).returns(eur)
      when(currencyDatabaseMock.getCurrency).expects(gpb.id).returns(gpb)
      when(currencyDatabaseMock.getCurrency).expects(aud.id).returns(aud)
      
      val listing = ExchangeRateListing(currencyDatabaseMock)
      
      val future: Future[Double] = listing.getExchangeRate(eur.id, gpb.id)

      future.map { exchangeRate =>
        assert(exchangeRate == eur.valueToUSD / gpb.valueToUSD)
    }
  }
}
```

## Sharing mocks and expectations in ScalaTest

Sometimes multiple test cases will require the same mocks (and more generally the same fixtures like databases, sockets). There are many ways to achieve that goal, Scala3Mock officialy support only two:

- _isolated test cases_: clean and simple, recommended when all tests have the same or very similar mocks
- _fixture contexts_: more flexible, recommended for complex test suites when a single set of mocks does not fit all test cases

### Isolated test cases

If you mix the `org.scalatest.OneInstancePerTest` trait to your test suite, each test case will be run in its own instance of the class suite. That means each test will get a different copy of the instance variables.

In the suite scope, you can
- declare instance variables (eg. mocks) that will be used by multiple test cases, and
- perform common test cases setup (eg. set up various mock expectations).

Because each test case has its own instance, different test cases do not interfere with each other.

> TODO Add a test case similar when working on the ScalaTest integration. Not sure how it's gonna
> work yet, given we are relying on implicit for the context propagation. Let's see.

```scala mdoc
import org.scalatest.OneInstancePerTest
import org.scalatest.matchers.should.Matchers

// Please note that this test suite mixes in OneInstancePerTest
class CoffeeMachineTest2 extends AnyFlatSpec with Matchers with OneInstancePerTest with MockFactory {
   // shared objects
   val waterContainerMock = mock[WaterContainer]
   val heaterMock = mock[Heater]
   val coffeeMachine = CoffeeMachine(waterContainerMock, heaterMock)
   
   // you can set common expectations in the suite scope
   when(() => heaterMock.isReady).expects().returning(true)
   
   // and perform common test setup
   coffeeMachine.powerOn()
   
   "CoffeeMachine" should "not turn on the heater when the water container is empty" in {
       coffeeMachine.isOn shouldBe true
       when(() => waterContainerMock.isEmpty).expects().returning(true)
        
       // ...
       coffeeMachine.powerOff()
       coffeeMachine.isOn shouldBe false
   }
   
   it should "not turn on the heater when the water container is overfull" in {
       // each test case uses a separate, fresh test suite so the coffee machine is turned on
       // even if previous test case turned it off
       coffeeMachine.isOn shouldBe true
       // ...
   }
}
```

Note that if you need to create costly fixtures (eg. establish a database connection), this strategy will duplicate that costly work by the amount of tests. In those cases, we instead recommend to use fixture contexts.

### Fixture contexts

Another way of sharing complex context between tests is to have separate fixture context. In practice that means creating a new trait/class that is instantiated per test. Because each test has its own instance, the context isn't shared between tests and because there is only one class suite you can use it to store common and expensive fixtures.

It's really easy to apply to regular test suites:

```scala mdoc

class CoffeeMachineTest3 extends AnyFlatSpec with Matchers with MockFactory {
   trait Test { // fixture context
       // shared objects
       val waterContainerMock = mock[WaterContainer]
       val heaterMock = mock[Heater]
       val coffeeMachine = CoffeeMachine(waterContainerMock, heaterMock)
   
       // test setup
       coffeeMachine.powerOn()
   }
   
   "CoffeeMachine" should "not turn on the heater when the water container is empty" in new Test {
       coffeeMachine.isOn shouldBe true
       when(() => waterContainerMock.isEmpty).expects().returning(true)
       // ...
   }
   
   // you can extend and combine fixture-contexts
   trait OverfullWaterContainerTest extends Test {
       // you can set expectations and use mocks in the fixture-context
       when(() => waterContainerMock.isOverfull).expects().returning(true)
   
       // and define helper functions
       def sharedComplexLogic(): Unit = {
         coffeeMachine.powerOff()
         // ...
       }
   }
   
   it should "not turn on the heater when the water container is overfull" in new OverfullWaterContainerTest {
       // ...
       sharedComplexLogic()
   }
}
```

This pattern can also be used with async test suite, but because those needs to return a `Future` (or equivalent) we cannot just return the fixture context directly.


```scala mdoc
class ExchangeRateListingTest2 extends AsyncFlatSpec with AsyncMockFactory {

  class Context {
    val eur = Currency(id = "EUR", valueToUSD = 1.0531, change = -0.0016)
    val gpb = Currency(id = "GPB", valueToUSD = 1.2280, change = -0.0012)
    val aud = Currency(id = "AUD", valueToUSD = 0.7656, change = -0.0024)

    val currencyDatabaseMock = mock[CurrencyDatabase]
    
    when(currencyDatabaseMock.getCurrency).expects(eur.id).returns(eur)
    when(currencyDatabaseMock.getCurrency).expects(gpb.id).returns(gpb)
    when(currencyDatabaseMock.getCurrency).expects(aud.id).returns(aud)
    
    val listing = ExchangeRateListing(currencyDatabaseMock)
  }


  "ExchangeRateListing" should "eventually return the exchange rate between passed Currencies when getExchangeRate is invoked" in {
      val ctx = Context()

      val future: Future[Double] = ctx.listing.getExchangeRate(ctx.eur.id, ctx.gpb.id)

      future.map { exchangeRate =>
        assert(exchangeRate == ctx.eur.valueToUSD / ctx.gpb.valueToUSD)
    }
  }
}
```


## Advanced

> TODO: how it works under the hood. Give context to debug some weird errors if the `MockContext` is
> either not available or corrupted.
