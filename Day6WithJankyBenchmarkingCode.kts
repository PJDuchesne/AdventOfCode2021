package advent

import advent.Day6WithJankyBenchmarkingCode.Constants.NEW_FISH_AGE
import advent.Day6WithJankyBenchmarkingCode.Constants.OLD_FISH_AGE
import advent.Day6WithJankyBenchmarkingCode.Constants.STEP_SIZE
import advent.Day6WithJankyBenchmarkingCode.Constants.TOTAL_DAYS
import java.io.File
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object Constants {
  const val OLD_FISH_AGE = 6
  const val NEW_FISH_AGE = 8
  const val STEP_SIZE = 1 // This should always evenly divide from 256
  const val TOTAL_DAYS = 256
}

println("\nhello\n")

main()

println("\ngoodbye\n")

@OptIn(kotlin.time.ExperimentalTime::class)
fun main() {
  val numberOfIterations = 10000

  val stepSizesToTry = listOf(1,2,4,8,16,32,64)

  // Warm up
  repeat(numberOfIterations) {
    partTwo(16)
  }

  val stepSizeOutputs = stepSizesToTry.associate { stepSize ->
    val solveTime = measureTimeMillis {
      repeat(numberOfIterations) {
        partTwo(stepSize)
      }
    }.toDuration(DurationUnit.MILLISECONDS)

    val operationTime = solveTime / numberOfIterations.toDouble()

    stepSize to operationTime
  }

  println("Finished")

  for ((stepSize, duration) in stepSizeOutputs) {
    println("Finished with stepSize $stepSize, averaging $duration per run")
  }

  println("Part Two Answer: ${partTwo(64)}") // 1605400130036
}

// Two parts:
// 1. Naively calculates how many fish a single fish will turn into after 64 (STEP_SIZE) days
// 2. Iterates through the total list 4 times (TOTAL_DAYS / STEP_SIZE), each time computing what each fish turns into
//    and calculating a new sum
fun partTwo(stepSize: Int): Long {
  // This maps what a singular fish would look like after STEP_SIZE days
  val batchResultFishByAge = (0..8).map { originalAge ->
    val batchOfFishes = mutableListOf(originalAge)

    repeat(stepSize) {
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

    sumByAge(batchOfFishes.toList())
  }

  // Iterate through
  require(TOTAL_DAYS % stepSize == 0)
  val numSteps = (TOTAL_DAYS / stepSize).toInt()

  // Running total of current fish by age, initialized to the original state
  var numByAge = sumByAge(getInitialState())

  val emptyArray = Array<Long>(NEW_FISH_AGE + 1) { 0L }

  var arrayOfAgedFish = (0..NEW_FISH_AGE).map { age ->
    numByAge.getOrElse(age) { 0L }
  }.toTypedArray()

  repeat(numSteps) {
    val newNumByAge = emptyArray.copyOf()

    for ((idx, oldNum) in arrayOfAgedFish.withIndex()) {
      val batchResultForThisAge = batchResultFishByAge[idx]

      for ((age, newNum) in batchResultForThisAge) {
        val newCount = newNumByAge[age] + oldNum * newNum
        newNumByAge[age] = newCount
      }
    }

    arrayOfAgedFish = newNumByAge
  }

  return arrayOfAgedFish.sumOf { it }
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
