package functions

class FunctionAdapter0[R](f: () => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 0)
    f()
  }
}

class FunctionAdapter1[T1, R](f: T1 => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 1)
    f(args.productElement(0).asInstanceOf[T1])
  }
}

class FunctionAdapter2[T1, T2, R](f: (T1, T2) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 2)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2])
  }
}

class FunctionAdapter3[T1, T2, T3, R](f: (T1, T2, T3) => R) extends Function1[Product, R] {

  def apply(args: Product) = {
    assert(args.productArity == 3)
    f(args.productElement(0).asInstanceOf[T1], args.productElement(1).asInstanceOf[T2], args.productElement(2).asInstanceOf[T3])
  }
}