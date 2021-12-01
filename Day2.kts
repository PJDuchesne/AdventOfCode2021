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
  val lines = File("Day2Input.txt").readLines()

  var vertical = 0
  var depth = 0

  for (line in lines) {
    val (direction, distance) = line.readHeading()

    when(direction) {
      "forward" -> vertical += distance
      "up" -> depth -= distance
      "down" -> depth += distance
      else -> throw IllegalArgumentException("Invalid argument: >>$direction<<")
    }
  }

  println("Vertical: >>$vertical<<, Depth: >>$depth<<") // 1906, 1017

  return vertical * depth // 1938402
}

fun partTwo(): Int {
  val lines = File("Day2Input.txt").readLines()

  var vertical = 0
  var depth = 0
  var aim = 0

  for (line in lines) {
    val (direction, distance) = line.readHeading()

    when(direction) {
      "forward" -> {
        vertical += distance
        depth += distance * aim
      }
      "up" -> aim -= distance
      "down" -> aim += distance
      else -> throw IllegalArgumentException("Invalid argument: >>$direction<<")
    }
  }

  println("Vertical: >>$vertical<<, Depth: >>$depth<<") // 1906, 1021972

  return vertical * depth // 1947878632
}

fun String.readHeading(): Pair<String, Int> {
  val (direction, distanceString) = this.split(" ", limit = 2) // split into at most two strings
  return Pair(direction, distanceString.toInt())
}
