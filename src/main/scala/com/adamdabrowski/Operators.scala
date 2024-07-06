package com.adamdabrowski


import scala.math.{abs, sqrt}

import cats.syntax.all.*


type Stack     = List[Double]
type Operation = Stack => Stack


enum Operators(val name: String, val operation: Operation):

  def apply(stack: Stack): Stack = operation(stack)

  case Add      extends Operators("+", binaryOperation(_ + _))
  case Subtract extends Operators("-", binaryOperation(_ - _))
  case Multiply extends Operators("*", binaryOperation(_ * _))
  case Divide   extends Operators("/", binaryOperation(_ / _))
  case Abs      extends Operators("abs", unaryOperation(abs))
  case Max      extends Operators("max", maximum)
  case Sqrt     extends Operators("sqrt", unaryOperation(sqrt))


object Operators:

  def parse(name: String): Option[Operators] =
    Operators.values.find(_.name.equalsIgnoreCase(name))


private def maximum: Operation =
  case stack @ head :: tail => stack.max :: Nil
  case _                    => throw IllegalArgumentException("Operation requires at least one operand.")


private def binaryOperation(operation: (Double, Double) => Double): Operation =
  case a :: b :: tail => operation(b, a) :: tail
  case _              => throw IllegalArgumentException("Operation requires two operands.")


private def unaryOperation(operation: Double => Double): Operation =
  case x :: tail => operation(x) :: tail
  case _         => throw IllegalArgumentException("Operation requires an operand.")
