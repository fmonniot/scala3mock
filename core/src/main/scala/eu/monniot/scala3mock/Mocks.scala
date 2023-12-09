package eu.monniot.scala3mock

object Mocks extends Mocks

/** Helper trait that provide access to all components (mandatory or optional)
  * used by the library to build mocks.
  */
trait Mocks
    extends functions.MockFunctions
    with macros.Mocks
    with matchers.Matchers:

  export main.withExpectations
