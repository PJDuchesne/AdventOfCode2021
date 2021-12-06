package advent

import advent.Day4.Constants.ANSI_GREEN
import advent.Day4.Constants.ANSI_RED
import advent.Day4.Constants.ANSI_RESET
import advent.Day4.Constants.BOARD_SIZE
import advent.Day4.Constants.MAX_VALUE
import java.io.File

object Constants {
  const val BOARD_SIZE = 5
  const val MAX_VALUE = 99
  const val ANSI_RESET = "\u001B[0m"
  const val ANSI_RED = "\u001B[31m"
  const val ANSI_GREEN = "\u001B[32m"
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}")
  println("Part Two Answer: ${partTwo()}")
}

fun partOne(): Int {
  val lines = File("Day4Input.txt").readLines()

  val boards = readBoards(lines.drop(1))

  // Each index position corresponds to a bingo value, from 0 to 99
  // The list tracks all the positions of all entries for that value
  val bingoPositions: List<MutableList<Pair<Int, Pair<Int, Int>>>> = (0..MAX_VALUE).map {
    mutableListOf()
  }

  // Iterate through each board, noting their positions (This could also be done while reading, :shrug:)
  for ((boardIdx, board) in boards.withIndex()) {
    for ((rowIdx, row) in board.withIndex()) {
      for ((colIdx, tile) in row.withIndex()) {
        bingoPositions[tile.value].add(Pair(boardIdx, Pair(rowIdx, colIdx)))
      }
    }
  }

  val bingoValues = lines.first().split(",").map { it.toInt() }
  for (value in bingoValues) {
    for ((boardIdx, coordinatePair) in bingoPositions[value]) {
      val (rowIdx, colIdx) = coordinatePair
      boards[boardIdx][rowIdx][colIdx].isMarked = true

      if (checkBoard(board = boards[boardIdx], originalRowIdx = rowIdx, originalColIdx = colIdx)) {
        return calculateFinalValue(
          board = boards[boardIdx],
          lastTileValue = boards[boardIdx][rowIdx][colIdx].value,
        )
      }
    }
  }

  throw IllegalStateException("Should have finished by now!")
}

fun partTwo(): Int {
  val lines = File("Day4Input.txt").readLines()

  val boards = readBoards(lines.drop(1))

  // Each index position corresponds to a bingo value, from 0 to 99
  // The list tracks all the positions of all entries for that value
  val bingoPositions: List<MutableList<Pair<Int, Pair<Int, Int>>>> = (0..MAX_VALUE).map {
    mutableListOf()
  }

  // Iterate through each board, noting their positions (This could also be done while reading, :shrug:)
  for ((boardIdx, board) in boards.withIndex()) {
    for ((rowIdx, row) in board.withIndex()) {
      for ((colIdx, tile) in row.withIndex()) {
        bingoPositions[tile.value].add(Pair(boardIdx, Pair(rowIdx, colIdx)))
      }
    }
  }

  val bingoValues = lines.first().split(",").map { it.toInt() }

  var latestFinalScore = 0
  var latestWinningBoardIdx = 0
  var winningBoardsCount = 0

  for (value in bingoValues) {
    for ((boardIdx, coordinatePair) in bingoPositions[value]) {
      // Skip empty boards
      if (boards[boardIdx].isEmpty()) continue

      val (rowIdx, colIdx) = coordinatePair
      boards[boardIdx][rowIdx][colIdx].isMarked = true

      if (checkBoard(board = boards[boardIdx], originalRowIdx = rowIdx, originalColIdx = colIdx)) {
        println("Printing Winning Board: $boardIdx (Val: ${boards[boardIdx][rowIdx][colIdx].value})")
        printBoard(boards[boardIdx])

        latestFinalScore = calculateFinalValue(
          board = boards[boardIdx],
          lastTileValue = boards[boardIdx][rowIdx][colIdx].value,
        )
        latestWinningBoardIdx = boardIdx
        winningBoardsCount += 1

        // Empty the board to avoid continuing to score it
        boards[boardIdx] = listOf()
      }
    }
  }

  println("The winning board was #$latestWinningBoardIdx (Total count $winningBoardsCount)")

  return latestFinalScore
}

// Returns true of the latest check scored Bingo!
fun checkBoard(board: List<List<BingoTile>>, originalRowIdx: Int, originalColIdx: Int): Boolean {
  // Check row
  if (board[originalRowIdx].all { it.isMarked }) return true

  // Check column
  var hasPassed = true
  for (rowIdx in (0..BOARD_SIZE - 1)) {
    if (!board[rowIdx][originalColIdx].isMarked) {
      hasPassed = false
      break
    }
  }

  return hasPassed
}

fun calculateFinalValue(board: List<List<BingoTile>>, lastTileValue: Int): Int {
  val sum = board.sumOf { row ->
    row.sumOf { tile ->
      if (tile.isMarked) {
        0
      } else {
        tile.value
      }
    }
  }

  return sum * lastTileValue
}

fun readBoards(lines: List<String>): MutableList<List<List<BingoTile>>>{
  return lines.chunked(BOARD_SIZE + 1)
    .map { boardLines ->
      boardLines.drop(1).map { line ->
        line.split(" ")
          .filter { it.isNotBlank()}
          .map { BingoTile(value = it.toInt()) }
      }
    }
    .toMutableList()
}

data class BingoTile(
  val value: Int,
  var isMarked: Boolean = false,
)

fun printBoard(board: List<List<BingoTile>>) {
  for (row in board) {
    row.print()
  }
}

fun List<BingoTile>.print() {
  println(this.joinToString(separator = "") { it.toPrintString() })
}

fun BingoTile.toPrintString(): String {
  val valueString = if (this.value < 10) {
    " ${this.value}"
  } else {
    this.value.toString()
  }

  val color = if (this.isMarked) {
    ANSI_GREEN
  } else {
    ANSI_RED
  }

  return "$color$valueString$ANSI_RESET "
}

