package advent

import advent.Day13.Constants.BACKGROUND_ASCII
import advent.Day13.Constants.FOLD_STRING
import advent.Day13.Constants.RESET_ASCII
import java.io.File

object Constants {
  val FOLD_STRING = "fold along "
  val BACKGROUND_ASCII = "\u001b[47m"
  val RESET_ASCII = "\u001b[0m"
}

val globalGrid = MutableList<MutableList<Boolean>>(0) { mutableListOf<Boolean>() }
var globalMaxRowIdx = 0
var globalMaxColIdx = 0

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 770
  println("Part Two Answer: (See below)") // EPUELPBR
  partTwo()
}

fun partOne(): Int {
  val folds = parseInput()
  foldPaper(folds.first())

  return countDots()
}

fun partTwo() {
  val folds = parseInput()

  for (fold in folds) {
    foldPaper(fold)
  }

  // Solution is then clearly visible in output
  printGrid()
}

fun foldPaper(fold: Pair<Int?, Int?>) {
  val foldRowIdx = fold.first
  val foldColIdx = fold.second

  when {
    foldRowIdx != null -> foldRow(foldRowIdx)
    foldColIdx != null -> foldCol(foldColIdx)
    else -> throw IllegalStateException("Invalid fold! $fold")
  }
}

fun foldRow(foldRowIdx: Int) {
  // Check state before folding
  require(globalGrid[foldRowIdx].all { !it })
  require(foldRowIdx < globalGrid.size - 1)

  // Do folding
  val minRowIdx = foldRowIdx + 1

  for (rowIdx in minRowIdx..globalMaxRowIdx) {
    val newRowIdx = foldRowIdx - (rowIdx - foldRowIdx)
    for (colIdx in 0..globalMaxColIdx) {
      if (globalGrid[rowIdx][colIdx]) globalGrid[newRowIdx][colIdx] = true
    }
  }

  // Remove extra paper
  globalMaxRowIdx = foldRowIdx - 1
}

fun foldCol(foldColIdx: Int) {
  // Check state before folding
  for (rowIdx in (0..globalMaxRowIdx)) {
    require(!globalGrid[rowIdx][foldColIdx])
  }
  require(foldColIdx < globalMaxColIdx) { "$foldColIdx, $globalMaxColIdx" }

  // Do folding
  val minColIdx = foldColIdx + 1

  // Iterate over all entries being folded over, storing the dot beneath if necessary
  for (rowIdx in 0..globalMaxRowIdx) {
    for (colIdx in minColIdx..globalMaxColIdx) {
      val newColIdx = foldColIdx - (colIdx - foldColIdx)
      if (globalGrid[rowIdx][colIdx]) globalGrid[rowIdx][newColIdx] = true
    }
  }

  // Remove extra paper
  globalMaxColIdx = foldColIdx - 1
}

fun countDots(): Int {
  // Only take the rows that are still on the paper
  return globalGrid.asSequence().take(globalMaxRowIdx + 1)
    .map { row ->
      // Only take columns that are still on the paper
      row.asSequence()
        .take(globalMaxColIdx + 1)
        .filter { it }
        .count()
    }
    .sumOf { it }
}

fun parseInput(): List<Pair<Int?, Int?>> {
  globalGrid.clear()

  val lines = File("Day13Input.txt").readLines()

  val emptyLineNumber = lines.indexOfFirst { it.isBlank() }

  val coordinates = lines.subList(0, emptyLineNumber)
    .map { line ->
      val (xString, yString) = line.split(',', limit = 2)
      Pair(xString.toInt(), yString.toInt())
    }

  val folds = lines.subList(emptyLineNumber + 1, lines.size)
    .map { line ->
      val instructionString = line.substringAfter(FOLD_STRING)
      val value = instructionString.substringAfter('=').toInt()

      when (instructionString.first()) {
        'x' -> Pair(null, value)
        'y' -> Pair(value, null)
        else -> throw IllegalArgumentException("Invalid line: >>$line<<")
      }
    }

  // Find the largest globalGrid size
  val coordinatesPlusFolds = coordinates + folds.map { Pair(it.first ?: 0, it.second ?: 0) }
  val xMax = coordinatesPlusFolds.maxByOrNull { it.first }!!.first
  val yMax = coordinatesPlusFolds.maxByOrNull { it.second }!!.second

  repeat(yMax + 1) {
    globalGrid.add(MutableList<Boolean>(xMax + 2) { false }) // Plus 2 for sketchy reasons
  }

  for ((colIdx, rowIdx) in coordinates) {
    globalGrid[rowIdx][colIdx] = true
  }

  globalMaxRowIdx = globalGrid.lastIndex
  globalMaxColIdx = globalGrid.first().lastIndex

  return folds
}

// Only print the portion of the grid exposed by globalMaxRowIdx / globalMaxColIdx
fun printGrid() {
  println("\nGrid:")
  for (row in globalGrid.subList(0, globalMaxRowIdx + 1)) {
    println(
      row.subList(0, globalMaxColIdx + 1)
        .joinToString("") { if (it) "X" else "$BACKGROUND_ASCII $RESET_ASCII" }
    )
  }
}
