package org.flowpaint.pixelprocessor

import _root_.org.flowpaint.brush.Brush
import _root_.scala.collection.immutable.HashMap
import _root_.scala.collection.Map
import codehaus.janino.{ScriptEvaluator, ClassBodyEvaluator, Scanner}
import java.io.StringReader
import model.Path
import util.{DataSample, CollectionUtils}

/**
 *
 *
 * @author Hans Haggstrom
 */

class ScanlineCalculator {
  private var program: PixelProgram = null

  private var variableNames: List[String] = Nil
  private var nameToIndexMap: Map[String, Int] = null
  private var variablesAtScanlineStart: Array[Float] = null
  private var variableValueDeltas: Array[Float] = null
  private var variableWorkingValues: Array[Float] = null

  def init(path: Path) {
    val emptyMap = new HashMap[String, String]()
    variableNames = (path.pixelProcessors flatMap ( processor => {processor.getUsedVariableNames(emptyMap)})).removeDuplicates

    nameToIndexMap = CollectionUtils.listToIndexMap(variableNames)

    def createVariableArray = new Array[Float](numberOfVariables)

    variablesAtScanlineStart = createVariableArray
    variableValueDeltas = createVariableArray
    variableWorkingValues = createVariableArray

    createProgram( path.pixelProcessors, variableNames, nameToIndexMap )

  }

  def calculateScanline(start: DataSample, end: DataSample,
                       outputBuffer: Array[Int],
                       outputOffset: Int,
                       scanlineLength: Int) {

    if (scanlineLength > 0) {

      def copyInitialVariables(target: Array[Float], data: DataSample) {
        variableNames foreach (name => {
          target(nameToIndexMap(name)) = data.getProperty(name, 0f)
        })
      }

      // Get start and end values for variables
      copyInitialVariables(variablesAtScanlineStart, start)
      copyInitialVariables(variableValueDeltas, end)

      // NOTE: By passing in initial value and delta (instead of value at start and end),
      // we make it easier to later use the same pixel program code to generate only the parts
      // of scanlines that fall within some screen surface block

      // Initialize the deltas to apply each step
      if (scanlineLength == 1) {
        // If start and end are on the same pixel, just average the start and end values
        (0 until numberOfVariables) foreach (i => {
          variablesAtScanlineStart(i) = 0.5f * (variablesAtScanlineStart(i) + variableValueDeltas(i))
        })
      }
      else {
        // Calculate offset that should be applied to each input variable for each step on the scanline
        (0 until numberOfVariables) foreach (i => {
          variableValueDeltas(i) = (variableValueDeltas(i) - variablesAtScanlineStart(i)) / (scanlineLength - 1).toFloat
        })

      }

      // Render the scanline
      program.calculateScanline(
        variablesAtScanlineStart,
        variableValueDeltas,
        variableWorkingValues,
        outputBuffer,
        outputOffset,
        scanlineLength )
    }
  }

  private def createProgram( processors : List[PixelProcessor],
                               variableNames : List[String],
                               nameToIndex : Map[String,Int] ): PixelProgram = {


    val source = createSource( processors, variableNames, nameToIndex )

    try{

      val evaluator = ClassBodyEvaluator.createFastClassBodyEvaluator(
        new Scanner( null, new StringReader( source ) ),
        classOf[PixelProgram],
        null )

      evaluator.asInstanceOf[PixelProgram]
    } catch {
      case e : Throwable => e.printStackTrace ; null  // TODO: Better logging?
    }
  }


  private def createSource( processors : List[PixelProcessor],
                            variableNames : List[String],
                            nameToIndex : Map[String,Int] ) : String = {

    val source = new StringBuilder()

    source append
    """
    void calculateScanline( float[] initialVariableValues,
                            float[] variableValueDeltas,
                            float[] workingArray,
                            int[] destinationBuffer,
                            int startOffset,
                            int scanlineLength ) {
    """

    // TODO: Generate source for each pizel processor

    source append "}"

    source.toString

  }

  private def numberOfVariables = variableNames.size

}