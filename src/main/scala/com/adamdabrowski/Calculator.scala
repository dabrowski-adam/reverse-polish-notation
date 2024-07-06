package com.adamdabrowski


import scala.annotation.tailrec
import scala.util.Try

import cats.syntax.all.*


object Calculator:

  private type Element = Operators | Double

  def calculate(input: String): Either[String, Double] =
    for
      elements <- parse(input)
      result   <- Try(evaluateRPN(elements)).toEither.leftMap(_.getMessage)
    yield result

  @tailrec
  private def evaluateRPN(remaining: List[Element], stack: Stack = List.empty): Double =
    (remaining, stack) match
      case (Nil, result :: Nil)                   => result
      case ((operand: Double) :: tail, stack)     => evaluateRPN(tail, operand :: stack)
      case ((operator: Operators) :: tail, stack) => evaluateRPN(tail, operator(stack))
      case (Nil, _)                               =>
        throw MatchError(
          "Stack contains multiple numbers but there are no operations left to perform.",
        )

  private def parse(input: String): Either[String, List[Element]] =
    for
      nonEmptyInput <- Option(input).filter(_.nonEmpty).toRight("Input cannot be empty.")
      words          = nonEmptyInput.trim.split("\\s+").toList
      parsed        <- words.traverse(parseWord).toRight("Input contains invalid elements.")
    yield parsed

  private def parseWord(word: String): Option[Element] =
    Operators.parse(word).orElse(word.toDoubleOption)
