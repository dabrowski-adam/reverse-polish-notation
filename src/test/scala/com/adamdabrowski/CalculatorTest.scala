package com.adamdabrowski


import org.scalacheck.Gen
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableFor2
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks


class CalculatorTest extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks:

  "Calculator" when:
    import com.adamdabrowski.Calculator.calculate

    "input is valid" should:

      "sum numbers" in:
        forAll: (a: Float, b: Float) =>
          calculate(s"$a $b +") shouldBe (a + b)

      "subtract numbers" in:
        forAll: (a: Float, b: Float) =>
          calculate(s"$a $b -") shouldBe (a - b)

      "multiply numbers" in:
        forAll: (a: Float, b: Float) =>
          calculate(s"$a $b *") shouldBe (a * b)

      "divide numbers" in:
        forAll: (a: Float, b: Float) =>
          calculate(s"$a $b /") shouldBe (a / b)

      "take an absolute value of a number" in:
        forAll: (a: Float) =>
          calculate(s"$a abs") shouldBe scala.math.abs(a)

      "find largest number" in:
        val number  = Gen.chooseNum(Float.MinValue, Float.MaxValue)
        val numbers = Gen.nonEmptyListOf(number)

        forAll(numbers): (xs: List[Float]) =>
          calculate(s"${xs.mkString(" ")} max") shouldBe xs.max

      "handle real numbers" in:
        calculate("0.5 2 *") shouldBe 1

      "handle negative numbers" in:
        calculate("-1 2 *") shouldBe -2

      "handle multiple operations" in:
        val inputs: TableFor2[String, Float] =
          Table(
            ("input",                      "expected result"),
            ("1 1 + 1 +",                  3f),
            ("12 2 3 4 * 10 5 / + * +",    40f),
            ("2 7 + 3 / 14 3 - 4 * + 2 /", 23.5f),
          )

        forAll(inputs): (input: String, expected: Float) =>
          calculate(input) shouldBe expected

      "handle combined unary, binary and n-ary operations" in:
        val inputs: TableFor2[String, Float] =
          Table(
            ("input",                  "expected result"),
            ("1 2 - abs",              1f),
            ("1 1 + 1 max",            2f),
            ("1 1 - 1 - abs 0 -1 max", 1f),
          )

        forAll(inputs): (input: String, expected: Float) =>
          calculate(input) shouldBe expected

end CalculatorTest
