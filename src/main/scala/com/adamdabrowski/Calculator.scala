package com.adamdabrowski

import scala.annotation.tailrec


enum Operators:

  case Number(n: Float) extends Operators
  case Add              extends Operators
  case Subtract         extends Operators
  case Multiply         extends Operators
  case Divide           extends Operators


object Operators:

  def calculate(a: Float, b: Float, operation: Operators): Float =
    operation match
      case Add      => a + b
      case Subtract => a - b
      case Multiply => a * b
      case Divide   => a / b
      case _        => throw new MatchError("Unsupported operation.")


object Calculator:

  def parse(element: String): Operators =
    element match
      case "+" => Operators.Add
      case "-" => Operators.Subtract
      case "*" => Operators.Multiply
      case "/" => Operators.Divide
      case n   => Operators.Number(n.toFloat)

  def calculate(input: String): Float =
    import com.adamdabrowski.Operators.*

    val operations: List[Operators] = input.split(" ").toList.map(parse)

    @tailrec
    def calculateTailrec(
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

    calculateTailrec(operations)
