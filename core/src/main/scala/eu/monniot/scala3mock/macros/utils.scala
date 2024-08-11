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

  def findMethodsToOverride(using quotes: Quotes)(
      sym: quotes.reflect.Symbol
  ): List[quotes.reflect.Symbol] =
    import quotes.reflect.*

    val objectMembers = Symbol.requiredClass("java.lang.Object").methodMembers
    val anyMembers = Symbol.requiredClass("scala.Any").methodMembers

    // First we refine the methods by removing the methods inherited from Object and Any
    val candidates = sym.methodMembers
      .filter { m =>
        !(objectMembers.contains(m) || anyMembers.contains(m))
      }
      .filterNot(_.flags.is(Flags.Private)) // Do not override private members

    // We then generate a list of methods to ignore for default values. We do this because the
    // compiler generate methods (following the `<methodName>$default$<parameterPosition>` naming
    // scheme) to hold the default value of a parameter (and insert them automatically at call site).
    // We do not want to override those.
    val namesToIgnore = candidates
      .flatMap { sym =>
        sym.paramSymss
          .filterNot(_.exists(_.isType))
          .flatten
          .zipWithIndex
          .collect {
            case (parameter, position)
                if parameter.flags.is(Flags.HasDefault) =>
              s"${sym.name}$$default$$${position + 1}"
          }
      }

    candidates.filterNot(m => namesToIgnore.contains(m.name))
  end findMethodsToOverride
