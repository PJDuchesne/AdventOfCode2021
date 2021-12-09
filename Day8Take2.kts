package advent

import advent.Day8Take2.Constants.EMPTY_MAP
import advent.Day8Take2.Constants.EXPECTED_COUNTS
import advent.Day8Take2.Constants.PROPER_WIRING

import java.io.File

object Constants {
  val EMPTY_MAP = mutableMapOf<Char, Int>(
    'a' to 0,
    'b' to 0,
    'c' to 0,
    'd' to 0,
    'e' to 0,
    'f' to 0,
    'g' to 0,
  )

  // Expected counts based on all 10 digits, plus 10x 1, plus 1x 7, and minus 1x 4
  // The intention here is to make every value uniquely identifiable without having to do further computations.
  // The extra digits (1/4/7) can be easily added because their values are immediately known, due to their unique segment length
  val EXPECTED_COUNTS = mapOf(
    4 to 'e',
    6 to 'd',
    5 to 'b',
    7 to 'g',
    9 to 'a',
    18 to 'c',
    19 to 'f',
  )

  val PROPER_WIRING = mapOf(
    "abcefg".toSet() to '0',
    "cf".toSet() to '1',
    "acdeg".toSet() to '2',
    "acdfg".toSet() to '3',
    "bcdf".toSet() to '4',
    "abdfg".toSet() to '5',
    "abdefg".toSet() to '6',
    "acf".toSet() to '7',
    "abcdefg".toSet() to '8',
    "abcdfg".toSet() to '9',
  )
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part Two Answer: ${partTwo()}") // 908067
}

fun partTwo(): Int {
  val lines = File("Day8Input.txt").readLines()

  return lines.sumOf { line ->
    computeAnswer(line)
  }
}

fun computeAnswer(line: String): Int {
  val (input, output) = line.split(" | ", limit = 2)

  val inputStrings = input.split(" ")

  val oneSet = inputStrings.single { it.length == 2 }.toSet()
  val fourSet = inputStrings.single { it.length == 4 }.toSet()
  val sevenSet = inputStrings.single { it.length == 3 }.toSet()

  val frequencies = countFrequencies(input)

  // Special magic (See comment at top of file)
  frequencies.plusFreq(oneSet, 10)
  frequencies.minusFreq(fourSet, 1)
  frequencies.plusFreq(sevenSet, 1)

  // Maps the output values to their original wiring state, which can then be interpreted using 'PROPER_WIRING'
  val wiringMap = frequencies.mapValues { (_, value) -> EXPECTED_COUNTS[value]!! }

  val answer = output.split(" ")
    .map {
      val charSet = it.toList().map { char -> wiringMap[char]!! }.toSet()
      PROPER_WIRING[charSet]!!
    }
    .joinToString("")
    .toInt()

  return answer
}

fun countFrequencies(input: String): MutableMap<Char, Int> {
  val frequencies = EMPTY_MAP.toMutableMap()

  for (char in input) {
    if (char == ' ') continue
    frequencies[char] = frequencies[char]!! + 1
  }

  return frequencies
}

fun MutableMap<Char, Int>.plusFreq(set: Set<Char>, mul: Int) {
  for (char in set) {
    this[char] = this[char]!! + mul
  }
}

fun MutableMap<Char, Int>.minusFreq(set: Set<Char>, mul: Int) {
  for (char in set) {
    this[char] = this[char]!! - mul
  }
}
