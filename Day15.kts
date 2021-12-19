package advent

import advent.Day15.Constants.DIRECTIONS
import advent.Day15.Constants.GRID_REPETITIONS
import java.io.File
import java.util.PriorityQueue

object Constants {
  // rowIdx then colIdx
  val DIRECTIONS = listOf(
    Pair(0, -1),
    Pair(1, 0),
    Pair(0, 1),
    Pair(-1, 0),
  )

  val GRID_REPETITIONS = 5 - 1
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 583
  println("Part Two Answer: ${partTwo()}") // 2729
}

fun partOne(): Long {
  val positionCosts = parsePartOne()
  return `🅰️⭐️`(positionCosts)
}

fun partTwo(): Long {
  val positionCosts = parsePartTwo()
  return `🅰️⭐️`(positionCosts)
}

fun `🅰️⭐️`(positionCosts: List<List<Long>>): Long {
  // Starting at 0,0 has a cost of 0
  val knownCosts = mutableMapOf(Pair(0, 0) to 0L)

  // Priority queue that always has the lowest total score at its head
  val positionsToCheck: PriorityQueue<Pair<Long, Pair<Int, Int>>> = PriorityQueue<Pair<Long, Pair<Int, Int>>>(
    compareBy { it.first }
  )

  positionsToCheck.add(Pair(0L, Pair(0, 0)))

  val goalPosition = Pair(positionCosts.lastIndex, positionCosts.first().lastIndex)

  while (positionsToCheck.isNotEmpty()) {
    val position = positionsToCheck.remove().second
    val currentCost = knownCosts.get(position)!!

    for (direction in DIRECTIONS) {
      val newPosition = position + direction
      val newPositionCost = positionCosts.getOrNull(newPosition) ?: continue

      val existingCost = knownCosts.getOrElse(newPosition) { Long.MAX_VALUE }
      val potentialNewCost = currentCost + newPositionCost

      // Short circuit the end condition, the first result will always be the best
      if (newPosition == goalPosition) return potentialNewCost

      if (potentialNewCost < existingCost) {
        knownCosts.put(newPosition, potentialNewCost)

        positionsToCheck.add(Pair(potentialNewCost, newPosition))
      }
    }
  }

  throw IllegalStateException("Should have found a proper path by now")
}

fun parsePartOne(): List<List<Long>> {
  return File("Day15Input.txt").readLines().map { row ->
    row.toList().map { it.digitToInt().toLong() }
  }
}

fun parsePartTwo(): List<List<Long>> {
  // Make grid wider to the right
  val wideGrid = parsePartOne().map { row ->
    var newRowSegment = row
    var fullRow = row

    repeat(GRID_REPETITIONS) {
      newRowSegment = newRowSegment.bumpCosts()
      fullRow += newRowSegment
    }

    fullRow
  }.toMutableList()

  // Repeat grid down
  val originalHeight = wideGrid.size
  for (idx in (0..originalHeight * GRID_REPETITIONS)) {
    wideGrid.add(wideGrid[idx].bumpCosts())
  }

  return wideGrid
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
  return Pair(this.first + other.first, this.second + other.second)
}

// Only null if out of bounds. All values should be defined within the board
fun List<List<Long>>.getOrNull(position: Pair<Int, Int>): Long? {
  val rowIdx = position.first
  val colIdx = position.second

  if (rowIdx < 0 || rowIdx >= this.size) return null
  if (colIdx < 0 || colIdx >= this.first().size) return null

  return this[position.first][position.second]
}

fun List<Long>.bumpCosts(): List<Long> = this.map { if (it == 9L) 1L else it + 1L }
