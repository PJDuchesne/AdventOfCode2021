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
  var count = 0

  val lines = File("Day1Input.txt").readLines()

  var previousDepth = lines[0].toInt()
  var currentDepth: Int

  for (line in lines.drop(1)) {
    currentDepth = line.toInt()

    if (currentDepth > previousDepth) {
      count++
    }

    previousDepth = currentDepth
  }

  return count // 1446
}

fun partTwo(): Int {
  var count = 0

  val lines = File("Day1Input.txt").readLines()

  var windowC = lines[2].toInt()
  var windowB = lines[1].toInt() + windowC
  var windowA = lines[0].toInt() + windowB

  for ((idx, line) in lines.drop(3).withIndex()) {
    val currentDepth = line.toInt()

    when (idx % 3) {
      0 -> {
        if (windowB + currentDepth > windowA) count++
        windowA = 0
      }
      1 -> {
        if (windowC + currentDepth > windowB) count++
        windowB = 0
      }
      2 -> {
        if (windowA + currentDepth > windowC) count++
        windowC = 0
      }
    }

    windowA += currentDepth
    windowB += currentDepth
    windowC += currentDepth
  }

  return count // 1486
}
