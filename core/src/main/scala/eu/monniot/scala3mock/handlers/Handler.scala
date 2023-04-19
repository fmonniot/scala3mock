package eu.monniot.scala3mock.handlers

import eu.monniot.scala3mock.context.Call

trait Handler:

  def handle(call: Call): Option[Any]

  def verify(call: Call): Boolean

  def isSatisfied: Boolean

  def reset(): Unit
