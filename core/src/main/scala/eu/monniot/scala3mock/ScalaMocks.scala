package eu.monniot.scala3mock

object ScalaMocks extends ScalaMocks

/** Helper trait that provide access to all components (mandatory or optional)
  * used by the library to build mocks.
  */
trait ScalaMocks
    extends functions.MockFunctions
    with macros.Mocks
    with matchers.Matchers:

  export main.withExpectations
