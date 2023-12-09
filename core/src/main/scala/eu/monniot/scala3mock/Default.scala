package eu.monniot.scala3mock

/** Provides a default value for some type. This is used for mocked functions to
  * return something if no specific value have beeen provided.
  *
  * Note that `Any` doesn't have any default at the moment. If you decide to
  * implement `Default` for your own type, do note that the library's mock macro
  * currently doesn't support higher-kinded types and default to `null` for
  * those. If this is necessary for you, we welcome Pull Requests!
  */
trait Default[A]:
  val default: A

object Default:

  def apply[A](using d: Default[A]): Default[A] = d

  // AnyVal
  given Default[Byte] with { val default: Byte = 0 }
  given Default[Short] with { val default: Short = 0 }
  given Default[Char] with { val default: Char = 0 }
  given Default[Int] with { val default = 0 }
  given Default[Long] with { val default = 0L }
  given Default[Float] with { val default = 0.0f }
  given Default[Double] with { val default = 0.0d }
  given Default[Boolean] with { val default = false }
  given Default[Unit] with { val default = () }

  // AnyRef
  given Default[java.io.OutputStream] with {
    val default = java.io.OutputStream.nullOutputStream()
  }
  given [Y]: Default[Y] with { val default = null.asInstanceOf[Y] }
