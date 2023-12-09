package eu.monniot.scala3mock.main

import eu.monniot.scala3mock.context.{Call, MockContext}
import eu.monniot.scala3mock.functions.MockFunction1
import eu.monniot.scala3mock.handlers.{CallHandler, Handler, UnorderedHandlers}

import scala.annotation.unused
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

class TestExpectationEx(message: String, methodName: Option[String])
    extends Throwable:
  override def getMessage(): String = message

