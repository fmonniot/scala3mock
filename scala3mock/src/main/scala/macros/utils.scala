package macros

object utils {
  private lazy val debug = sys.props.get("scala3mock.debug.macros").contains("true")
  def debug(x: String): Unit =
    if debug then println(x)
}
