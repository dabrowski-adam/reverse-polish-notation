package com.adamdabrowski


import scala.util.chaining.scalaUtilChainingOps

import com.adamdabrowski.Calculator.calculate


object App:

  def main(args: Array[String]): Unit =
    calculate(args.mkString(" "))
      .map(_.toString pipe dropTrailingZeros)
      .fold(println, println)

  private def dropTrailingZeros(string: String): String =
    if string.contains('.') then string.reverse.dropWhile(Set('0', '.').contains).reverse
    else string
