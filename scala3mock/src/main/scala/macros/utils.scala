package macros

object utils {
  private lazy val debug = sys.props.get("scala3mock.debug.macros").isDefined
  def debug(x: String): Unit =
    if (debug) println(x)
}
