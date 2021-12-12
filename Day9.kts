package advent

import advent.Day9.Constants.DIRECTIONS
import advent.Day9.Constants.GRID_EDGE
import advent.Day9.Constants.GRID_MAX
import java.io.File

object Constants {
  const val GRID_EDGE = 10 // grid edge is considered higher
  const val GRID_MAX = 9

  val DIRECTIONS = listOf(
    Pair(0, -1),
    Pair(1, 0),
    Pair(0, 1),
    Pair(-1, 0),
  )
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 548
  println("Part Two Answer: ${partTwo()}") // 786048
}

fun partOne(): Int {
  val lines = File("Day9Input.txt").readLines()
  val grid = parseGrid(lines)

  var lowRisk = 0

  for (rowIdx in 1..grid.size - 2) { // Weird bounds because of tmp row
    for (colIdx in 1..grid.first().size - 2) {
      if (grid.isLowPoint(rowIdx, colIdx)) {
        lowRisk += grid[rowIdx][colIdx] + 1
      }
    }
  }

  return lowRisk // 1658 too low
}

fun partTwo(): Int {
  val lines = File("Day9Input.txt").readLines()
  val grid = parseGrid(lines)

  // Find low points
  val lowPoints = mutableListOf<Pair<Int, Int>>()
  for (rowIdx in 1..grid.size - 2) { // Weird bounds because of tmp row
    for (colIdx in 1..grid.first().size - 2) {
      if (grid.isLowPoint(rowIdx, colIdx)) {
        lowPoints.add(Pair(rowIdx, colIdx))
      }
    }
  }

  // Find trough for each low point
  val lowPointSizes = lowPoints.map {
    grid.parseLowPoint(it)
  }

  // Return size of largest 3 basins multiplied together
  return lowPointSizes.sortedDescending()
    .take(3)
    .reduce { acc, size -> acc * size }
}

fun parseGrid(lines: List<String>): List<MutableList<Int>> {
  val inputGrid = lines.map {
    (listOf(GRID_EDGE) + it.map { it.digitToInt() } + listOf(GRID_EDGE)).toMutableList()
  }

  val width = inputGrid.first().size
  val edgeRow = MutableList<Int>(width) { GRID_EDGE }

  return listOf(edgeRow) + inputGrid + listOf(edgeRow)
}

fun List<MutableList<Int>>.isLowPoint(rowIdx: Int, colIdx: Int): Boolean {
  val value = this[rowIdx][colIdx]
  if (value == GRID_EDGE) return false

  return DIRECTIONS.all { (rowDir, colDir) ->
    value < this[rowIdx + rowDir][colIdx + colDir]
  }
}

// Actually mutating the original grid is fine because every point is in exactly 0 or 1 basin
fun List<MutableList<Int>>.parseLowPoint(lowPoint: Pair<Int, Int>): Int {
  var size = 0
  val spotsToCheck = ArrayDeque<Pair<Int, Int>>()
  spotsToCheck.add(lowPoint)

  while (spotsToCheck.isNotEmpty()) {
    val (rowIdx, colIdx) = spotsToCheck.removeFirst()
    val value = this[rowIdx][colIdx]

    // Make this value a mountain to avoid looking here again
    this[rowIdx][colIdx] = GRID_EDGE

    if (value >= GRID_MAX) continue

    size += 1

    // Add adjacent spots to check
    for ((rowIdxDelta, colIdxDelta) in DIRECTIONS) {
      val newRowIdx = rowIdx + rowIdxDelta
      val newColIdx = colIdx + colIdxDelta

      val newValue = this[newRowIdx][newColIdx]

      if (newValue < GRID_EDGE) {
        spotsToCheck.add(Pair(newRowIdx, newColIdx))
      }
    }
  }

  return size
}
