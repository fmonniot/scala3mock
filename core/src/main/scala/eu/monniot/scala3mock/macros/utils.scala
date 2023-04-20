package eu.monniot.scala3mock.macros

import scala.quoted.Quotes

private[macros] object utils:

  def sortSymbolsViaSignature(using quotes: Quotes)(
      list: List[quotes.reflect.Symbol]
  ): List[quotes.reflect.Symbol] =
    import quotes.reflect.*
    list.sortBy { sym =>
      val sig = sym.signature
      sig.resultSig + sig.paramSigs.map(_.toString()).mkString
    }
