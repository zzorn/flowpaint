package org.flowpaint.brush

import _root_.org.flowpaint.ink.PixelProcessorMetadata
import _root_.org.flowpaint.property.Data
import _root_.scala.collection._
import pixelprocessors.{PixelProcessor, PixelProgram}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

object PixelProcessorCompiler {

  /**
   * Creates a PixelProgram that can be used to calculate the effect of applying the specified PixelProcessors in sequence for a specific pixel data.
   *
   * Returns the compiled PixelProgram, and the mapping to use for storing input variables in the input data array.
   */
  def compile( brushSettings : Data,
               pixelProcessors : List[PixelProcessor],
               requestedOutputVariables : List[String] ) : (PixelProgram, Map[String,Int]) = {

    val (source, inputMap) = generateSource(brushSettings, pixelProcessors, requestedOutputVariables)

    // TODO: Compile

    null
  }

  def generateSource(brushSettings : Data,
               pixelProcessors : List[PixelProcessor],
               requestedOutputVariables : List[String]) : (String, Map[String,Int]) = {

    // Collect variables
    val usedVariables = new mutable.HashSet[String]()
    pixelProcessors foreach (_.getUsedVariables() foreach (usedVariables += _))

    // Create variable mapping
    var variableToIndexMap = Map[String, Int]
    var i = 0
    usedVariables foreach { variableToIndexMap += ( _ -> i) ; i += 1  }

    // 
  }

}