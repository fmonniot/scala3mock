package eu.monniot.scala3mock.scalatest

import eu.monniot.scala3mock.context.{Mock, MockContext}
import eu.monniot.scala3mock.macros.{MockImpl, WhenImpl}
import eu.monniot.scala3mock.functions.*
import eu.monniot.scala3mock.main.TestExpectationEx
import eu.monniot.scala3mock.handlers.UnorderedHandlers
import scala.collection.mutable.ListBuffer
import eu.monniot.scala3mock.context.Call

import org.scalatest.{AsyncTestSuite, AsyncTestSuiteMixin, FutureOutcome}
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import org.scalatest.Exceptional

trait AsyncMockFactory extends AsyncTestSuiteMixin with BaseFactory:
  this: AsyncTestSuite => // To prevent users from using Sync with Async suites

  private def withExpectations[T](test: => Future[T]): Future[T] =
    initializeExpectations()
    val testResult = test.map { result =>
      verifyExpectations()
      result
    }

    testResult onComplete {
      case Success(_) => ()
      case Failure(_) => clearTestExpectations()
    }

    testResult

  abstract override def withFixture(test: NoArgAsyncTest): FutureOutcome =
    if (autoVerify)
      new FutureOutcome(
        withExpectations(super.withFixture(test).toFuture).recoverWith({
          case NonFatal(ex) => Future.successful(Exceptional(ex))
        })
      )
    else
      super.withFixture(test)
