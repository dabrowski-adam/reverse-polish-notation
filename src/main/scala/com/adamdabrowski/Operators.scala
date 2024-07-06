package com.adamdabrowski


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

  def parseOperator(element: String): Operators =
    element match
      case "+" => Operators.Add
      case "-" => Operators.Subtract
      case "*" => Operators.Multiply
      case "/" => Operators.Divide
      case n   => Operators.Number(n.toFloat)
