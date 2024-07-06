package com.adamdabrowski


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class CalculatorTest extends AnyWordSpec with Matchers:

  "Reverse Polish Notation" should:

    "add two numbers together" in:
      Calculator.calculate("1 1 +") shouldEqual 2

    "multiply two numbers together" in:
      Calculator.calculate("2 3 *") shouldEqual 6

    "calculate multiple operations" in:
      Calculator.calculate("2 7 + 3 / 14 3 - 4 * + 2 /") shouldEqual 23.5
