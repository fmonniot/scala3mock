package handlers

import context.Call

trait Handler:

  def handle(call: Call): Option[Any]

  def verify(call: Call): Boolean

  def isSatisfied: Boolean

  def reset(): Unit
