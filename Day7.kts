package advent

import java.io.File
import kotlin.math.abs
import kotlin.math.roundToInt

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}")
  println("Part Two Answer: ${partTwo()}")
}

fun partOne(): Int {
  val fishes = File("Day7Input.txt").readLines().single()
    .split(",")
    .map { it.toInt() }

  val fishesByPosition = fishes.groupBy { it }
      .mapValues { (_, values) ->
        values.size
      }

  val minPosition = fishesByPosition.keys.minOrNull()!!
  val maxPosition = fishesByPosition.keys.maxOrNull()!!

  var lowestValue = Int.MAX_VALUE

  for (targetPos in minPosition..maxPosition) {
    val cost = fishesByPosition.entries.sumOf { (pos, num) ->
      abs(pos - targetPos) * num
    }

    lowestValue = minOf(cost, lowestValue)
  }

  return lowestValue // 340987
}

fun partTwo(): Int {
  val fishes = File("Day7Input.txt").readLines().single()
    .split(",")
    .map { it.toInt() }

  val fishesByPosition = fishes.groupBy { it }
    .mapValues { (_, values) ->
      values.size
    }

  val minPosition = fishesByPosition.keys.minOrNull()!!
  val maxPosition = fishesByPosition.keys.maxOrNull()!!

  var lowestValue = Int.MAX_VALUE

  for (targetPos in minPosition..maxPosition) {
    val cost = fishesByPosition.entries.sumOf { (pos, num) ->
      (abs(pos - targetPos)).calcDistance() * num
    }

    lowestValue = minOf(cost, lowestValue)
  }

  return lowestValue // 96987874
}

fun Int.calcDistance(): Int = (this * this + this) / 2
