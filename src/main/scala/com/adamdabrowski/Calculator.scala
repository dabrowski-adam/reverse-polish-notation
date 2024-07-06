package com.adamdabrowski

import scala.annotation.tailrec


object Calculator:

  private type Element = Operators | Float

  def calculate(input: String): Float =
    evaluateRPN(parse(input))

  @tailrec
  private def evaluateRPN(remaining: List[Element], stack: Stack = List.empty): Float =
    (remaining, stack) match
      case (Nil, result :: Nil)                   => result
      case ((operand: Float) :: tail, stack)      => evaluateRPN(tail, operand :: stack)
      case ((operator: Operators) :: tail, stack) => evaluateRPN(tail, operator(stack))
      case (Nil, _)                               =>
        throw MatchError(
          "Stack contains multiple numbers but there are no operations left to perform.",
        )

  private def parse(input: String): List[Element] =
    val words = input.split("\\s+").toList
    words.map(parseWord)

  private def parseWord(word: String): Element =
    Operators.parse(word).getOrElse(word.toFloat)
