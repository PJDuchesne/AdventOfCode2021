package advent

import advent.Day12.Constants.START_NODE
import advent.Day12.Constants.END_NODE
import java.io.File

object Constants {
  val START_NODE = "start"
  val END_NODE = "end"
}

println("\nhello\n")

main()

println("\ngoodbye\n")

fun main() {
  println("Part One Answer: ${partOne()}") // 3410
  println("Part Two Answer: ${partTwo()}") // 98796
}

fun partOne(): Int {
  // Construct cave system
  val caveSystem = constructCaveSystem()

  var distinctPaths = 0
  val routesToExplore = ArrayDeque<List<String>>()

  routesToExplore.add(listOf(START_NODE))

  while(routesToExplore.isNotEmpty()) {
    val currentRoute = routesToExplore.removeFirst()

    for (adjacentCave in caveSystem.get(currentRoute.last())!!) {
      if (adjacentCave == START_NODE) continue
      if (adjacentCave == END_NODE) {
        distinctPaths += 1
        continue
      }

      // Do not revisit small caves twice
      if (adjacentCave.isSmallCave() && currentRoute.contains(adjacentCave)) continue

      routesToExplore.add(currentRoute.plus(adjacentCave))
    }
  }

  return distinctPaths
}

fun partTwo(): Int {
  // Construct cave system
  val caveSystem = constructCaveSystem()

  var distinctPaths = 0
  val routesToExplore = ArrayDeque<PartTwoRoute>()

  routesToExplore.add(PartTwoRoute(path = listOf(START_NODE)))

  while(routesToExplore.isNotEmpty()) {
    val currentRoute = routesToExplore.removeFirst()
    val path = currentRoute.path

    for (adjacentCave in caveSystem.get(currentRoute.path.last())!!) {
      var canVisitAnotherSmallCave = currentRoute.canVisitAnotherSmallCave

      if (adjacentCave == START_NODE) continue
      if (adjacentCave == END_NODE) {
        distinctPaths += 1
        continue
      }

      // Only revisit a single small cave once per run
      if (adjacentCave.isSmallCave() && currentRoute.path.contains(adjacentCave)) {
        if (canVisitAnotherSmallCave) {
          canVisitAnotherSmallCave = false
        } else {
          continue
        }
      }

      routesToExplore.add(
        PartTwoRoute(
          path = path.plus(adjacentCave),
          canVisitAnotherSmallCave = canVisitAnotherSmallCave
        )
      )
    }
  }

  return distinctPaths
}

fun constructCaveSystem(): Map<String, List<String>> {
  val lines = File("Day12Input.txt").readLines()

  val caveSystem = mutableMapOf<String, MutableList<String>>()
  for (line in lines) {
    val (start, end) = line.splitLine()

    val startConnections = caveSystem.getOrPut(start) { mutableListOf() }
    val endConnections = caveSystem.getOrPut(end) { mutableListOf() }

    startConnections.add(end)
    endConnections.add(start)
  }

  return caveSystem
}

fun String.splitLine(): Pair<String, String> {
  val (startString, endString) = this.split("-")
  return Pair(startString, endString)
}

fun List<String>.currentCave(): String = this.last()

// This assumes the input is well formatted
fun String.isSmallCave(): Boolean = this.first().isLowerCase()

data class PartTwoRoute(
  val path: List<String>,
  val canVisitAnotherSmallCave: Boolean = true,
)
