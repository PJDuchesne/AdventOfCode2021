package advent

import java.io.File

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}")
  println("Part Two Answer: ${partTwo()}")
}

fun partOne(): Int {
  val lines = File("Day3Input.txt").readLines()

  val frequencies = MutableList<Int>(lines.first().length) { 0 }
  for (line in lines) {
    for ((idx, char) in line.withIndex()) {
      frequencies[idx] += char.toFreq()
    }
  }

  val gammaBinary = frequencies.map { freq ->
    when {
      freq > 0 -> 1
      freq < 0 -> 0
      else -> throw IllegalStateException("Not defined in problem")
    }
  }

  val epsilonBinary = gammaBinary.map { if (it > 0) 0 else 1 }

  val gamma = gammaBinary.binaryToDec() // 2795
  val epsilon = epsilonBinary.binaryToDec() // 1300

  return gamma * epsilon * gamma // 3633500
}

fun partTwo(): Int {
  val lines = File("Day3Input.txt").readLines()

  val oxygenRating = filterForBest(lines, true).binaryToDec() // 1327
  val carbonDioxideRating = filterForBest(lines, false).binaryToDec() // 3429

  return oxygenRating * carbonDioxideRating // 4550283
}

// Oxygen rating will be found with `mostCommon` = true, while CO2 will be found with `mostCommon` = false
fun filterForBest(lines: List<String>, mostCommon: Boolean): String {
  val remainingLines = lines.toMutableList()
  val binaryWidth = lines.first().length

  for (idx in (0..binaryWidth - 1)) {
    val frequency = remainingLines.sumOf { it[idx].toFreq() }

    val desiredBit = if (mostCommon) {
      if (frequency >= 0) '1' else '0'
    } else {
      if (frequency < 0) '1' else '0'
    }

    val backup = remainingLines.last()
    remainingLines.removeAll { it[idx] != desiredBit }

    if (remainingLines.isEmpty()) {
      println("Returning backup value: >>$backup<<")
      return backup
    }

    if (remainingLines.size == 1) {
      println("Returning last value: >>${remainingLines.single()}<<")
      return remainingLines.single()
    }
  }

  throw IllegalStateException("Shouldn't get here!")
}

fun List<Int>.binaryToDec(): Int {
  return this.reversed().asSequence()
    .withIndex()
    .sumOf { (idx, char) -> char * Math.pow(2.0, idx.toDouble()).toInt() }
}

fun String.binaryToDec(): Int {
  return this.toList()
    .map { it.digitToInt() }
    .binaryToDec()
}

fun Char.toFreq(): Int {
  return when (this) {
    '0' -> -1
    '1' -> 1
    else -> throw IllegalArgumentException("Found invalid character >>$this<<")
  }
}