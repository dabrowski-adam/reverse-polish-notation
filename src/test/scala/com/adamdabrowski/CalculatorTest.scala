package com.adamdabrowski


import scala.math.{abs, sqrt}

import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.EitherValues
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks


class CalculatorTest extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with EitherValues:

  "Calculator" when:
    import com.adamdabrowski.Calculator.calculate

    "input is valid" should:

      "sum numbers" in:
        forAll: (a: Double, b: Double) =>
          calculate(s"$a $b +").value shouldBe (a + b)

      "subtract numbers" in:
        forAll: (a: Double, b: Double) =>
          calculate(s"$a $b -").value shouldBe (a - b)

      "multiply numbers" in:
        forAll: (a: Double, b: Double) =>
          calculate(s"$a $b *").value shouldBe (a * b)

      "divide numbers" in:
        forAll: (a: Double, b: Double) =>
          calculate(s"$a $b /").value shouldBe (a / b)

      "take an absolute value of a number" in:
        forAll: (a: Double) =>
          calculate(s"$a abs").value shouldBe abs(a)

      "find largest number" in:
        val number  = Gen.chooseNum(Double.MinValue, Double.MaxValue)
        val numbers = Gen.nonEmptyListOf(number)

        forAll(numbers): (xs: List[Double]) =>
          calculate(s"${xs.mkString(" ")} max").value shouldBe xs.max

      "take the square root of a number" in:
        forAll: (a: Double) =>
          whenever(a > .0):
            calculate(s"$a sqrt").value shouldEqual sqrt(a)

      "handle real numbers" in:
        calculate("0.5 2 *").value shouldBe 1.0

      "handle negative numbers" in:
        calculate("-1 2 *").value shouldBe -2.0

      "handle multiple operations" in:
        val inputs =
          Table(
            ("input",                      "expected result"),
            ("1 1 + 1 +",                  3.0),
            ("12 2 3 4 * 10 5 / + * +",    40.0),
            ("2 7 + 3 / 14 3 - 4 * + 2 /", 23.5),
          )

        forAll(inputs): (input: String, expected: Double) =>
          calculate(input).value shouldBe expected

      "handle combined unary, binary and n-ary operations" in:
        val inputs =
          Table(
            ("input",                  "expected result"),
            ("1 2 - abs",              1.0),
            ("1 1 + 1 max",            2.0),
            ("1 1 - 1 - abs 0 -1 max", 1.0),
          )

        forAll(inputs): (input: String, expected: Double) =>
          calculate(input).value shouldBe expected

      "handle whitespace" in:
        val whitespace  = Gen.oneOf(' ', '\t')
        val validInputs =
          for
            left    <- Gen.stringOf(whitespace)
            operand  = 42
            middle  <- Gen.nonEmptyStringOf(whitespace)
            operator = "abs"
            right   <- Gen.stringOf(whitespace)
          yield s"$left$operand$middle$operator$right"

        forAll(validInputs): (input: String) =>
          calculate(input).value shouldBe 42.0

    "input is invalid" should:

      "fail when input is empty" in:
        calculate("").left.value shouldBe "Input cannot be empty."

      "fail for unknown operators" in:
        calculate("1 1 unknown").left.value shouldBe "Input contains invalid elements."

      "fail when there are not enough operands for a binary operator" in:
        val invalidInputs: Gen[String] =
          for
            maybeOperand <- Gen.option(Gen.double)
            operator     <- Gen.oneOf(List("+", "-", "*", "/"))
            input         = maybeOperand.map(operand => s"$operand $operator").getOrElse(operator)
          yield input

        forAll(invalidInputs): input =>
          calculate(input).left.value shouldBe "Operation requires two operands."

      "fail when there are not enough operands for an unary operator" in:
        calculate("abs").left.value shouldBe "Operation requires an operand."

end CalculatorTest
