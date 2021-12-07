package advent

import advent.Day5.Constants.ANSI_GREEN
import advent.Day5.Constants.ANSI_RED
import advent.Day5.Constants.ANSI_RESET
import java.io.File

object Constants {
  const val ANSI_RESET = "\u001B[0m"
  const val ANSI_RED = "\u001B[31m"
  const val ANSI_GREEN = "\u001B[32m"
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${solve(isPartTwo = false)}") // 8350
  println("Part Two Answer: ${solve(isPartTwo = true)}") // 19374
}

fun solve(isPartTwo: Boolean): Int {
  val vents = parseVents()
  val maxSize = determineMaxSize(vents)

  val ocean = (0..maxSize).map {
    MutableList<Int>(maxSize + 1) { 0 }
  }

  for (vent in vents) {
    val direction = vent.determineDirection(isPartTwo) ?: continue

    var currentPosition = vent.start
    while(currentPosition != vent.end) {
      ocean[currentPosition.rowIdx][currentPosition.colIdx] += 1
      currentPosition += direction
    }

    ocean[currentPosition.rowIdx][currentPosition.colIdx] += 1

//    printOcean(ocean)
  }

  return countIntersections(ocean)
}

fun parseVents(): List<Vent> {
  val lines = File("Day5Input.txt").readLines()

  return lines.map { ventString ->
    val (startString, endString) = ventString.split(" -> ", limit = 2)

    val startValues = startString.split(",", limit = 2).map { it.toInt() }
    val endValues = endString.split(",", limit = 2).map { it.toInt() }

    Vent(
      start = Cord(rowIdx = startValues.last(), colIdx = startValues.first()),
      end = Cord(rowIdx = endValues.last(), colIdx = endValues.first()),
    )
  }
}

fun determineMaxSize(vents: List<Vent>): Int {
  return vents.flatMap { listOf(it.start.rowIdx, it.start.colIdx, it.end.colIdx, it.end.rowIdx) }
    .maxByOrNull { it }!!
}

fun countIntersections(ocean: List<MutableList<Int>>): Int {
  return ocean.map { row ->
    row.count { it > 1 }
  }.sumOf { it }
}

// returns the vector to travel from start to finish
fun Vent.determineDirection(isPartTwo: Boolean): Cord? {
  return when {
    start.rowIdx == end.rowIdx -> {
      if (start.colIdx > end.colIdx) { // i.e. "8,4 -> 8,0"
        Cord(0, -1)
      } else { // i.e. "8,0 -> 8,4"
        Cord(0, 1)
      }
    }
    start.colIdx == end.colIdx -> {
      if (start.rowIdx > end.rowIdx) { // i.e. "4,8 -> 0,8"
        Cord(-1, 0)
      } else { // i.e. "0,8 -> 4,8"
        Cord(1, 0)
      }
    }
    !isPartTwo -> null

    // This assumes that the vents are properly formatted. This could be done by using the result of this check to check along the line that they interesct start/end
    else -> {
      val xDir = if (start.rowIdx > end.rowIdx) -1 else 1
      val yDir = if (start.colIdx > end.colIdx) -1 else 1
      Cord(xDir, yDir)
    }
  }
}

fun printOcean(ocean: List<MutableList<Int>>) {
  println("")
  for (row in ocean) {
    val rowString = row.joinToString("") { value ->
      when(value) {
        0 -> "."
        1 -> "${ANSI_RED}$value${ANSI_RESET}"
        else -> "${ANSI_GREEN}$value${ANSI_RESET}"
      }
    }
    println(rowString)
  }
  println("")
}

data class Vent(
  val start: Cord,
  val end: Cord,
)

data class Cord(
  val rowIdx: Int,
  val colIdx: Int,
) {
  operator fun plus(other: Cord): Cord {
    return Cord(
      rowIdx = this.rowIdx + other.rowIdx,
      colIdx = this.colIdx + other.colIdx,
    )
  }

  operator fun minus(other: Cord): Cord {
    return Cord(
      rowIdx = this.rowIdx - other.rowIdx,
      colIdx = this.colIdx - other.colIdx,
    )
  }
}
