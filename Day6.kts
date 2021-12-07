package advent

import advent.Day6.Constants.NEW_FISH_AGE
import advent.Day6.Constants.OLD_FISH_AGE
import advent.Day6.Constants.STEP_SIZE
import advent.Day6.Constants.TOTAL_DAYS
import java.io.File

object Constants {
  const val OLD_FISH_AGE = 6
  const val NEW_FISH_AGE = 8
  const val STEP_SIZE = 64 // This should always evenly divide from 256
  const val TOTAL_DAYS = 256
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 353079
  println("Part Two Answer: ${partTwo()}") // 1605400130036
}

// Purely naive (Does not scale to part 2 size)
fun partOne(): Int {
  val fishes = getInitialState()
    .toMutableList()

  repeat(80) {
    val numberOfFish = fishes.size
    for (idx in (0..numberOfFish - 1)) {
      when (fishes[idx]) {
        0 -> {
          fishes[idx] = OLD_FISH_AGE
          fishes.add(NEW_FISH_AGE)
        }
        else -> {
          fishes[idx] -= 1
        }
      }
    }
  }

  return fishes.size
}

// Two parts:
// 1. Naively calculates how many fish a single fish will turn into after 64 (STEP_SIZE) days
// 2. Iterates through the total list 4 times (TOTAL_DAYS / STEP_SIZE), each time computing what each fish turns into
//    and calculating a new sum
fun partTwo(): Long {
  // This maps what a singular fish would look like after STEP_SIZE days
  val batchResultFishByAge = (0..8).associate { originalAge ->
    val batchOfFishes = mutableListOf(originalAge)

    repeat(STEP_SIZE) {
      val numberOfFish = batchOfFishes.size
      for (idx in (0..numberOfFish - 1)) {
        when (batchOfFishes[idx]) {
          0 -> {
            batchOfFishes[idx] = OLD_FISH_AGE
            batchOfFishes.add(NEW_FISH_AGE)
          }
          else -> {
            batchOfFishes[idx] -= 1
          }
        }
      }
    }

    originalAge to sumByAge(batchOfFishes.toList())
  }

  // Iterate through
  require(TOTAL_DAYS % STEP_SIZE == 0)
  val numSteps = (TOTAL_DAYS / STEP_SIZE).toInt()

  // Running total of current fish by age, initialized to the original state
  var numByAge = sumByAge(getInitialState())

  repeat(numSteps) {
    val newNumByAge = (0..NEW_FISH_AGE)
      .associate { it to 0.toLong() }
      .toMutableMap()

    for ((idx, oldNum) in numByAge) {
      val batchResultForThisAge = batchResultFishByAge.get(idx)!!

      for ((age, newNum) in batchResultForThisAge) {
        val newCount = newNumByAge[age]!! + oldNum * newNum
        newNumByAge.put(age, newCount)
      }
    }

    numByAge = newNumByAge
  }

  return numByAge.values.sumOf { it }
}

fun getInitialState(): List<Int> {
  return File("Day6Input.txt").readLines().single()
    .split(",")
    .map { it.toInt() }
}

fun sumByAge(fishes: List<Int>): Map<Int, Long> {
  return fishes.groupBy { it }
    .mapValues { (_, values) ->
      values.size.toLong()
    }
}
