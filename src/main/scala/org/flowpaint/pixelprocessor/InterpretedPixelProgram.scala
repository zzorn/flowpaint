package org.flowpaint.pixelprocessor
/**
 * Manually coded software version
 *
 * @author Hans Haggstrom
 * @deprecated not needed really, apart from initial testing / reference
 */
class InterpretedPixelProgram( pixelProcessors : List[PixelProcessor] ) extends PixelProgram {

  

  def calculateScanline(variablesAtScanlineStart: Array[Float],
                       variablesAtScanlineEnd: Array[Float],
                       workingArray: Array[Float],
                       destinationBuffer: Array[Int],
                       startOffset: Int,
                       scanlineLength: Int) {

    val numberOfVariables = variablesAtScanlineStart.length

    // Reuse the arrays to store current values and step deltas
    val currentValues = variablesAtScanlineStart
    val valueStepDeltas = variablesAtScanlineEnd

    // Initialize the deltas to apply each step
    if (scanlineLength == 1) {
      // If start and end are on the same pixel, just average the start and end values
      (0 until numberOfVariables) foreach (i => {
        currentValues(i) = 0.5f * (variablesAtScanlineStart(i) + variablesAtScanlineEnd(i))
      })
    }
    else {
      // Calculate offset that should be applied to each input variable for each step on the scanline
      (0 until numberOfVariables) foreach (i => {
        valueStepDeltas(i) = (variablesAtScanlineEnd(i) - variablesAtScanlineStart(i)) / (scanlineLength - 1).toFloat
      })

    }

    // Render the scanline
    var i = 0
    while (i < scanlineLength) {

      // TODO

      i += 1
    }

  }
}