package eu.monniot.scala3mock.scalatest

import context.{Mock, MockContext}
import macros.{MockImpl, WhenImpl}
import functions._
import main.TestExpectationEx
import handlers.UnorderedHandlers
import scala.collection.mutable.ListBuffer
import context.Call

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
