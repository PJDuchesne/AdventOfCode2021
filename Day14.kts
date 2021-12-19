package advent

import advent.Day14.Constants.POLY_STRING
import java.io.File

object Constants {
  val POLY_STRING = " -> "
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${growPolymers(10)}") // 4517
  println("Part Two Answer: ${growPolymers(40)}") // 4704817645083
}

fun growPolymers(steps: Int): Long {
  val (polymerTemplate, pairInsertions) = parseInput()

  // Setup initial pairMap state
  var pairMap = pairInsertions.keys.associate { it to 0L }
    .toMutableMap()

  for (idx in 0..polymerTemplate.length - 2) {
    val polymerPair = polymerTemplate.polymerAt(idx)
    val currentValue = pairMap.get(polymerPair)!!

    pairMap.put(polymerPair, currentValue + 1L)
  }

  // Run permutations
  repeat(steps) {
    var tmpMap = pairMap

    // Iterate through polymer pairs that currently exist
    for ((polymerPair, count) in pairMap.filter { it.value > 0 }) {
      tmpMap.put(polymerPair, tmpMap.get(polymerPair)!! - count)

      for (additionalPolymerPair in pairInsertions.get(polymerPair)!!) {
        tmpMap.put(additionalPolymerPair, tmpMap.get(additionalPolymerPair)!! + count)
      }
    }

    // Sanity check
    require(tmpMap.all { it.value >= 0 })

    pairMap = tmpMap
  }

  return calculateAnswer(polymerTemplate, pairMap)
}

fun calculateAnswer(originalTemplate: String, pairMap: Map<String, Long>): Long {
  val elementToCount = pairMap
    .filter { it.value > 0 }
    .map { (polymerPair, count) ->
      polymerPair.map { char -> char to count }
    }
    .flatten()
    .groupBy({ it.first }, { it.second })
    .mapValues { it.value.sum() }
    .toMutableMap()

  // Add first and last char one more time
  val firstChar = originalTemplate.first()
  elementToCount.put(firstChar, elementToCount.get(firstChar)!! + 1)

  val lastChar = originalTemplate.last()
  elementToCount.put(lastChar, elementToCount.get(lastChar)!! + 1)

  val sortedElementDistributions = elementToCount
    .values
    .sorted()

  // Divide by two because the values are all double counted above
  return (sortedElementDistributions.last() - sortedElementDistributions.first()) / 2L
}

fun parseInput(): ParsedInput {
  val lines = File("Day14Input.txt").readLines()

  val polymerTemplate = lines.first()

  val pairInsertions = lines.drop(2)
    .associate { line ->
      val (input, output) = line.split(POLY_STRING, limit = 2)
      require(input.length == 2)
      require(output.length == 1)

      val output1 = "${input[0]}$output"
      val output2 = "$output${input[1]}"

      input to listOf(output1, output2)
    }

  return ParsedInput(
    polymerTemplate = polymerTemplate,
    pairInsertions = pairInsertions,
  )
}

fun String.polymerAt(idx: Int): String = this.substring(idx, idx + 2)

data class ParsedInput(
  val polymerTemplate: String,
  val pairInsertions: Map<String, List<String>>,
)

