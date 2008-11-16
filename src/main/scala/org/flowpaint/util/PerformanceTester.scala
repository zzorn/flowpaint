package org.flowpaint.util

import scala.compat.Platform.currentTime


/**
 *
 * @author Hans Haggstrom
 */
object PerformanceTester {

  /**
   * Runs a function multiple times and times the duration.  Prints measured times to stdout.
   *
   * @returns the average time used by the function.
   */
  def timeFunction( numberOfRuns : Int, functionToTest : () => Unit ) : Long = {

    def runTest( number : Int ): Long = {
      val startTime = currentTime

      functionToTest()

      val testRunTime = currentTime - startTime
      println("  Test run " + number + " finished in " + testRunTime + " ms.")

      testRunTime
    }

    var sum = 0L
    for (i <- 0 until numberOfRuns) sum += runTest( i )
    
    val average = sum / numberOfRuns

    println("Average duration of a test run: " + average + " ms")

    average
  }

  private var startTime = 0L
  def startTimer()  {startTime = currentTime}
  def showTiming(taskName : String) = println( taskName + " took " + (currentTime - startTime) +" ms.")

  /**
   * Takes the task name as a parameter, and should be followed by the task to time
   * (returns a function that takes the task to time as a parameter). 
   */
  def time[T](taskName : String)(task : => T) {

    startTimer()

    task

    showTiming(taskName)
  }


}