package org.flowpaint.brush

import org.flowpaint.ink.PixelProcessorMetadata
import org.flowpaint.property.Data
import scala.collection._
import scala.collection
import scala.collection.immutable.HashMap
import org.flowpaint.pixelprocessor.{PixelProgram}
import org.flowpaint.pixelprocessor.{PixelProcessor}

/**
 * 
 * 
 * @author Hans Haggstrom
 * @deprecated not used
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
    val emptyMap = new HashMap[String, String]()  
    val usedVariables = new mutable.HashSet[String]()
    pixelProcessors foreach (_.getUsedVariableNames( emptyMap ) foreach (usedVariables += _))

    // Create variable mapping
    val variableToIndexMap = new mutable.HashMap[String, Int]
    var i = 0
    usedVariables foreach { s : String => variableToIndexMap += ( s -> i) ; i += 1  }

    // TODO: Generate source
    val source = ""

    ( source, variableToIndexMap )
  }

}