package advent

import advent.Day11.Constants.DIRECTIONS
import advent.Day11.Constants.MAX_ENERGY
import advent.Day11.Constants.NUMBER_OF_STEPS
import java.io.File

object Constants {
  val NUMBER_OF_STEPS = 100

  val MAX_ENERGY = 10

  // Directions, including diagonal
  val DIRECTIONS = listOf(
    Pair(0, -1),
    Pair(1, 0),
    Pair(0, 1),
    Pair(-1, 0),
    Pair(-1, -1),
    Pair(1, 1),
    Pair(-1, 1),
    Pair(1, -1),
  )
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 1656
  println("Part Two Answer: ${partTwo()}") // 256
}

fun partOne(): Int {
  val grid = parseGrid()

  var numberOfFlashes = 0
  repeat(NUMBER_OF_STEPS) {
    numberOfFlashes += grid.step()
  }

  return numberOfFlashes
}

// Totally naive
fun partTwo(): Int {
  val grid = parseGrid()

  val totalOctupuses = grid.size * grid.first().size

  var step = 0
  while(true) {
    step += 1
    if (grid.step() == totalOctupuses) break
  }

  return step
}

fun parseGrid(): List<MutableList<Int>> {
  return File("Day11Input.txt").readLines().map {
    it.map { it.digitToInt() }.toMutableList()
  }
}

fun List<MutableList<Int>>.step(): Int {
  val rowMax = this.size - 1
  val colMax = this.first().size - 1

  val alreadyFlashed = mutableSetOf<Pair<Int, Int>>()

  // Initially bump all values
  for (rowIdx in 0..rowMax) {
    for (colIdx in 0..colMax) {
      this[rowIdx][colIdx] += 1
    }
  }

  // Start flashing!

  var hasFlashedThisCheck: Boolean
  while (true) {
    hasFlashedThisCheck = false

    for (rowIdx in 0..rowMax) {
      for (colIdx in 0..colMax) {
        // Skip if this one has already flashed
        val cords = Pair(rowIdx, colIdx)
        if (alreadyFlashed.contains(cords)) continue

        // Check if this octupus should flash
        if (this[rowIdx][colIdx] >= MAX_ENERGY) {
          hasFlashedThisCheck = true
          alreadyFlashed.add(cords)
          this[rowIdx][colIdx] = 0

          for ((rowIdxDelta, colIdxDelta) in DIRECTIONS) {
            val newRowIdx = rowIdx + rowIdxDelta
            val newColIdx = colIdx + colIdxDelta

            if (newRowIdx < 0 || newRowIdx > rowMax) continue
            if (newColIdx < 0 || newColIdx > colMax) continue

            if (alreadyFlashed.contains(Pair(newRowIdx, newColIdx))) continue

            this[newRowIdx][newColIdx] += 1
          }
        }
      }
    }

    // Break if an iteration finishes without anything flashing
    if (!hasFlashedThisCheck) break
  }

  return alreadyFlashed.size
}

