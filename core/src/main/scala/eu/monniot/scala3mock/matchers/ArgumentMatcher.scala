package eu.monniot.scala3mock.matchers

class ArgumentMatcher(template: Product) extends (Product => Boolean):

  def apply(args: Product) = template == args

  override def toString = template.productIterator.mkString("(", ", ", ")")
