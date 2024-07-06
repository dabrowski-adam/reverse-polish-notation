package com.adamdabrowski


import scala.annotation.tailrec

import com.adamdabrowski.Operators.*


object Calculator:

  def calculate(input: String): Float =
    calculateTailrec(parse(input))

  private def parse(input: String): List[Operators] =
    input.split(" ").toList.map(parseOperator)

  @tailrec
  private def calculateTailrec(
    remaining: List[Operators],
    stack:     List[Float] = List.empty,
  ): Float =
    if remaining.isEmpty then stack.head
    else
      remaining.head match
        case Number(n) => calculateTailrec(remaining.tail, n :: stack)
        case operation =>
          val first  = stack.head
          val second = stack.tail.head
          val result = Operators.calculate(second, first, operation)
          calculateTailrec(remaining.tail, result :: stack.tail.tail)
