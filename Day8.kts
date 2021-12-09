package advent

import advent.Day8.Constants.EASY_LENGTHS
import java.io.File

object Constants {
  val DIGIT_TO_LENGTHS = listOf(6, 2, 5, 5, 4, 5, 6, 3, 7, 6)
  val EASY_DIGITS = listOf(1, 4, 7, 8)
  val EASY_LENGTHS = listOf(2, 4, 3, 7)

  val DIGITS_BY_LENGTH = mapOf(
    2 to listOf(1),
    3 to listOf(7),
    4 to listOf(4),
    5 to listOf(2, 3, 5),
    6 to listOf(0, 6, 9),
    7 to listOf(8),
  )
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
//  println("Part Zer Answer: ${partZero()}")
//  println("Part One Answer: ${partOne()}")
  println("Part Two Answer: ${partTwo()}")
}

fun partZero(): Int {
  val lines = File("Day8Input.txt").readLines()

  for (line in lines) {
    val sortedLineStr = line.split(" | ", limit = 2)
      .map { it.split(" ") }
      .map { it.map { it.toSortedSet().joinToString("") } }
      .map { it.joinToString(" ")}
      .joinToString(" | ")

    println(sortedLineStr)
  }

  return 0
}

fun partOne(): Int {
  val lines = File("Day8Input.txt").readLines()

  var count = 0

  for (line in lines) {
    val (_, outputsByLength) = parseLine(line)

    val outputs = outputsByLength
      .values
      .flatMap { it }
      .filter { it.length in EASY_LENGTHS }

    count += outputs.size
  }

  return count
}

fun partTwo(): Int {
  val lines = File("Day8Input.txt").readLines()

  return lines.sumOf { line ->
    computeSolution(line)
  }
}

fun parseLine(line: String): Pair<Map<Int, List<String>>, Map<Int, List<String>>> {
  val (patternsByLength, outputsByLength) = line
    .split(" | ", limit = 2)
    .map { it.splitByLengths() }

  return Pair(patternsByLength, outputsByLength)
}

fun String.splitByLengths(): Map<Int, List<String>> = this
  .split(" ")
  .map { it.toSortedSet().joinToString("") }
  .groupBy { it.length }
  .toMap()

fun computeSolution(line: String): Int {
  val (inputsByLength, _) = parseLine(line)

  // Initialize to initially known state
  val solutionMap = mutableMapOf<Set<Char>, Int>(
    inputsByLength[2]!!.single().toSet() to 1,
    inputsByLength[4]!!.single().toSet() to 4,
    inputsByLength[3]!!.single().toSet() to 7,
    inputsByLength[7]!!.single().toSet() to 8,
  )

  val lengthFive = inputsByLength[5]!!.toMutableSet()
  val lengthSix = inputsByLength[6]!!.toMutableSet()

  // Find 6
  val sevenSet = solutionMap.entries.single { it.value == 7 }.key
  val sixString = lengthSix.single {
    !it.toSet().containsAll(sevenSet)
  }

  solutionMap.put(sixString.toSet(), 6)

  lengthSix.remove(sixString)

  // Find 0 and 9
  val fourSet = solutionMap.entries.single { it.value == 4 }.key
  val nineString = lengthSix.single {
    it.toSet().containsAll(fourSet)
  }

  val zeroString = lengthSix.single {
    it != nineString
  }

  solutionMap.put(nineString.toSet(), 9)
  solutionMap.put(zeroString.toSet(), 0)

  // Find 3
  val threeString = lengthFive.single {
    it.toSet().containsAll(sevenSet)
  }

  solutionMap.put(threeString.toSet(), 3)
  lengthFive.remove(threeString)

  // Find 5
  val sixSet = solutionMap.entries.single { it.value == 6 }.key
  val fiveString = lengthFive.single {
    sixSet.containsAll(it.toSet())
  }

  solutionMap.put(fiveString.toSet(), 5)
  lengthFive.remove(fiveString)

  // Find 2 (remainder)
  val twoString = lengthFive.single()
  solutionMap.put(twoString.toSet(), 2)

//  println(solutionMap
//    .entries
//    .map { it.toPair() }
//    .sortedBy { it.second }
//    .map { it.first.joinToString("") to it.second }
//  )

  // Calculate answer

  val outputStrings = line.substringAfter(" | ")
    .split(" ")
    .map { it.toSortedSet().joinToString("") }

//  println("Outputs = $outputStrings")

  val usableSolutionMap = solutionMap.mapKeys { (key, _) ->
    key.joinToString("")
  }

//  println(usableSolutionMap)

  val answer = outputStrings.map {
    usableSolutionMap[it]!!.toInt()
  }
    .joinToString("")
    .toInt()

//  println("Answer is $answer")

  return answer
}
