package macros

import scala.quoted.Quotes

object utils:
  private lazy val debug = sys.props.get("scala3mock.debug.macros").contains("true")
  def debug(x: String): Unit =
    if debug then println(x)
  
  def sortSymbolsViaSignature(using quotes: Quotes)(list: List[quotes.reflect.Symbol]): List[quotes.reflect.Symbol] =
    import quotes.reflect.*
    list.sortBy { sym =>
      val sig = sym.signature
      sig.resultSig + sig.paramSigs.map(_.toString()).mkString
    }


