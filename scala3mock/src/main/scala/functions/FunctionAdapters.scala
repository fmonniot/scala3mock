package functions

class FunctionAdapter0[R](f: () => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 0)
    f()

class FunctionAdapter1[T1, R](f: T1 => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 1)
    f(args.productElement(0).asInstanceOf[T1])

class FunctionAdapter2[T1, T2, R](f: (T1, T2) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 2)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2])

class FunctionAdapter3[T1, T2, T3, R](f: (T1, T2, T3) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 3)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3])

class FunctionAdapter4[T1, T2, T3, T4, R](f: (T1, T2, T3, T4) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 4)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
    )

class FunctionAdapter5[T1, T2, T3, T4, T5, R](f: (T1, T2, T3, T4, T5) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 5)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
      args.productElement(4).asInstanceOf[T5],
    )

class FunctionAdapter6[T1, T2, T3, T4, T5, T6, R](f: (T1, T2, T3, T4, T5, T6) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 6)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
      args.productElement(4).asInstanceOf[T5],
      args.productElement(5).asInstanceOf[T6],
    )

class FunctionAdapter7[T1, T2, T3, T4, T5, T6, T7, R](f: (T1, T2, T3, T4, T5, T6, T7) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 7)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
      args.productElement(4).asInstanceOf[T5],
      args.productElement(5).asInstanceOf[T6],
      args.productElement(6).asInstanceOf[T7],
    )

class FunctionAdapter8[T1, T2, T3, T4, T5, T6, T7, T8, R](f: (T1, T2, T3, T4, T5, T6, T7, T8) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 8)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
      args.productElement(4).asInstanceOf[T5],
      args.productElement(5).asInstanceOf[T6],
      args.productElement(6).asInstanceOf[T7],
      args.productElement(7).asInstanceOf[T8],
    )

class FunctionAdapter9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 9)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
      args.productElement(4).asInstanceOf[T5],
      args.productElement(5).asInstanceOf[T6],
      args.productElement(6).asInstanceOf[T7],
      args.productElement(7).asInstanceOf[T8],
      args.productElement(8).asInstanceOf[T9],
    )

class FunctionAdapter10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](f: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => R) extends Function1[Product, R]:

  def apply(args: Product) =
    assert(args.productArity == 10)
    f(
      args.productElement(0).asInstanceOf[T1],
      args.productElement(1).asInstanceOf[T2],
      args.productElement(2).asInstanceOf[T3],
      args.productElement(3).asInstanceOf[T4],
      args.productElement(4).asInstanceOf[T5],
      args.productElement(5).asInstanceOf[T6],
      args.productElement(6).asInstanceOf[T7],
      args.productElement(7).asInstanceOf[T8],
      args.productElement(8).asInstanceOf[T9],
      args.productElement(9).asInstanceOf[T10],
    )
