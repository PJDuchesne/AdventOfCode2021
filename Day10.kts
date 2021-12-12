package advent

import advent.Day10.Constants.CLOSING_CHARS
import advent.Day10.Constants.OPENING_TO_CLOSING_CHAR
import advent.Day10.Constants.PART_1_POINT_VALUES
import advent.Day10.Constants.PART_2_POINT_VALUES
import advent.Day10.Constants.PART_2_SCORE_MULTIPLIER
import java.io.File

object Constants {
  val PART_1_POINT_VALUES = mapOf(
    ')' to 3L,
    ']' to 57L,
    '}' to 1197L,
    '>' to 25137L,
  )

  val PART_2_POINT_VALUES = mapOf(
    '(' to 1L,
    '[' to 2L,
    '{' to 3L,
    '<' to 4L,
  )

  val PART_2_SCORE_MULTIPLIER = 5L

  val OPENING_CHARS = listOf('(', '[', '{', '<')
  val CLOSING_CHARS = listOf(')', ']', '}', '>')

  val OPENING_TO_CLOSING_CHAR = OPENING_CHARS.zip(CLOSING_CHARS).toMap()
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 388713
  println("Part Two Answer: ${partTwo()}") //
}

fun partOne(): Long {
  val lines = File("Day10Input.txt").readLines()

  return lines.asSequence()
    .mapNotNull { line -> findIllegalCharacter(line) }
    .map { illegalChar -> PART_1_POINT_VALUES.get(illegalChar)!! }
    .sum()
}

fun partTwo(): Long {
  val lines = File("Day10Input.txt").readLines()

  val scores = lines.asSequence()
    .map { chopLine(it) }
    .filter { isChoppedLineLegal(it) }
    .map { computeScore(it) }
    .toList()
    .sorted()

  val winningIndex = (scores.size - 1) / 2

  return scores[winningIndex]
}

fun findIllegalCharacter(line: String): Char? {
  val choppedLine = chopLine(line)

  // The first remaining closing brace is ILLEGAL
  return choppedLine.firstOrNull { char ->
    CLOSING_CHARS.contains(char)
  }
}

fun isChoppedLineLegal(choppedLine: String): Boolean {
  return choppedLine.none { char ->
    CLOSING_CHARS.contains(char)
  }
}

// Remove all properly formatted chunks from within the string
fun chopLine(line: String): String {
  var index = 0
  var choppedLine = line

  while (index < choppedLine.length - 1) {
    val char = choppedLine[index]

    if (CLOSING_CHARS.contains(char)) {
      index += 1
      continue
    }

    val closingChar = OPENING_TO_CLOSING_CHAR.get(choppedLine[index])!!

    if (choppedLine[index + 1] == closingChar) {
      choppedLine = choppedLine.removeRange(startIndex = index, endIndex = index + 2)
      index -= 1
      if (index < 0) index = 0
    } else {
      index += 1
    }
  }

  return choppedLine
}

fun computeScore(choppedLine: String): Long {
  return choppedLine.reversed().toList().asSequence()
    .map { PART_2_POINT_VALUES[it]!! }
    .reduce { acc, score ->
      acc * PART_2_SCORE_MULTIPLIER + score
    }
}
