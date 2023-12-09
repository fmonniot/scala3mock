package eu.monniot.scala3mock

import eu.monniot.scala3mock.context.MockContext

object ScalaMocks extends ScalaMocks

/** Helper trait that provide access to all components (mandatory or optional)
  * used by the library to build mocks.
  */
trait ScalaMocks
    extends functions.MockFunctions
    with macros.Mocks
    with matchers.Matchers:

  // apparently using export in 3.2.2 lose the default value of the
  // parameter. That might have been fixed in 3.3+, but we can't use
  // that version so for now we will duplicate the definition.
  def withExpectations[A](verifyAfterRun: Boolean = true)(
      f: MockContext ?=> A
  ): A = eu.monniot.scala3mock.withExpectations(verifyAfterRun)(f)
