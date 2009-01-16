package org.flowpaint.pixelprocessor

import _root_.org.flowpaint.brush.Brush
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.immutable.HashMap
import _root_.scala.collection.Map
import codehaus.janino.{ScriptEvaluator, ClassBodyEvaluator, Scanner}
import java.io.StringReader
import model.Path
import util.{DataSample, CollectionUtils, PropertyRegister}
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

  def init(pixelProcessors : List[PixelProcessor], settings : Data) {
    val emptyMap = new HashMap[String, String]()
    variableNames = ((pixelProcessors flatMap ( processor =>
               { processor.getUsedVariableNames(emptyMap)})) :::
            settings.getFloatPropertyNames :::
            List( "red", "green", "blue", "alpha"  )
            ) .removeDuplicates

    nameToIndexMap = CollectionUtils.listToIndexMap(variableNames)

    def createVariableArray = new Array[Float](numberOfVariables)
    variablesAtScanlineStart = createVariableArray
    variableValueDeltas = createVariableArray
    variableWorkingValues = createVariableArray

    val source = createSource( pixelProcessors, variableNames, nameToIndexMap, settings )

/*
      // DEBUG
      println( "Compiling PixelProgram: /n" + source + "/n/n" )
*/

    program = compileProgram( source )
  }

  def calculateScanline(start: DataSample,
                       end: DataSample,
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
      program.calculateScanline( variablesAtScanlineStart,
                                    variableValueDeltas,
                                    variableWorkingValues,
                                    outputBuffer,
                                    outputOffset,
                                    scanlineLength )
    }
  }

  private def compileProgram( source : String ): PixelProgram = {

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
                            nameToIndex : Map[String,Int],
                            generalSettings : Data ) : String = {

    def createVariableGetterExpression( id : Int, default : Float ) : String = {
      val name = PropertyRegister.getName( id )
      if (nameToIndex.contains( name ))
        "workingArray[" + nameToIndex(name) + "]"
      else
        default.toString + "f"
    }


    var uniqueId = 0
    def nextUniqueId() : Int = { uniqueId += 1; uniqueId }


    val source = new StringBuilder()

    // Standard imports
    source append "import java.util.Random;\n"
    source append "import org.flowpaint.pixelprocessor.NoiseUtils;\n"


    val redExpression = createVariableGetterExpression( PropertyRegister.RED, 0 )
    val greenExpression = createVariableGetterExpression( PropertyRegister.GREEN, 0 )
    val blueExpression = createVariableGetterExpression( PropertyRegister.BLUE, 0 )
    val alphaExpression = createVariableGetterExpression( PropertyRegister.ALPHA, 1 )

    val variableNum = variableNames.size

    // Generate source for each pixel processor
    var classBodyCodes : List[String] = Nil
    var initCodes : List[String] = Nil
    var loopCodes : List[String] = Nil
    processors foreach ( processor => {
      val (classBodyCode, initCode, loopCode) =processor.generateCode( variableNames,
                                                        nameToIndex,
                                                        generalSettings,
                                                        "workingArray",
                                                        nextUniqueId )
      val comment = "\n      // " + processor.getClass.getName + "\n"
      classBodyCodes = classBodyCodes ::: List( comment, classBodyCode )
      initCodes = initCodes ::: List( comment, initCode )
      loopCodes = loopCodes::: List( comment, loopCode )
    })


    classBodyCodes foreach (source append _ )

    source append
    """
    // ############# Scanline calculation method
    public void calculateScanline( float[] currentVariableValues,
                            float[] variableValueDeltas,
                            float[] workingArray,
                            int[] destinationBuffer,
                            int startOffset,
                            int scanlineLength ) {

      float throwAwayValue = 0f;
     """
    // NOTE: throwAwayValue is used to assign values to if the correct variable index can not be found, to avoid runtime failure.

    initCodes foreach (source append _ )


    source append
     """

    // ######## Scanline calculation loop
      final int endIndex = startOffset + scanlineLength;
      for ( int i = startOffset; i < endIndex; i++ ) {

        // ## Interpolate variables
        for ( int j = 0; j < """ +variableNum+ """; j++ ) {
          workingArray[j] = currentVariableValues[j];
          currentVariableValues[j] += variableValueDeltas[j];
        }

    """

    loopCodes foreach (source append _ )


    source append
    """
        // #### Copy color values to output buffer

        float r = """+ redExpression +""";
        float g = """+ greenExpression +""";
        float b = """+ blueExpression +""";
        float a = """+ alphaExpression +""";

        if (r <  0f) r = 0f; else if ( r > 1f ) r = 1f;
        if (g <  0f) g = 0f; else if ( g > 1f ) g = 1f;
        if (b <  0f) b = 0f; else if ( b > 1f ) b = 1f;
        if (a <  0f) a = 0f; else if ( a > 1f ) a = 1f;

        int redInt = ((int) ( r * 255 ));
        int greenInt = ((int) ( g * 255 ));
        int blueInt = ((int) ( b * 255 ));
        int alphaInt = ((int) ( a * 255 ));

        if ( alphaInt > 0 ) {

          if ( alphaInt < 255 ) {
            final int originalColor = destinationBuffer[ i ];

            final int newFactor = (int) (256 * a);
            final int originalFactor = 256 - newFactor ;

            redInt   = ( ((originalColor >> 16) & 0xff) * originalFactor + redInt   * newFactor ) >> 8;
            greenInt = ( ((originalColor >> 8)  & 0xff) * originalFactor + greenInt * newFactor ) >> 8;
            blueInt  = ( ( originalColor        & 0xff) * originalFactor + blueInt  * newFactor ) >> 8;
          }


          destinationBuffer[ i ] =
                 ( 0xFF                << 24 ) |
                 ( ( redInt   & 0xFF ) << 16 ) |
                 ( ( greenInt & 0xFF ) << 8 ) |
                 ( ( blueInt  & 0xFF ) << 0 );
        }
      }
    }
    """

    source.toString

  }

  

  private def numberOfVariables = variableNames.size

}