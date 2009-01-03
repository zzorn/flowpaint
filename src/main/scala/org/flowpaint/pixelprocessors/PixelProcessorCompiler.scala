package org.flowpaint.brush

import _root_.org.flowpaint.ink.PixelProcessorMetadata
import _root_.org.flowpaint.property.Data
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
   * Returns the compiled PixelProgram, and the mapping to use for storing input variables in the input data array,
   * and the mapping to use to get the identifiers of the output variables based on their array position.
   */
  def compile( brushSettings : Data, pixelProcessors : List[PixelProcessor] ) : (PixelProgram, Map[String,Int], Map[Int,String]) = {
    null  
  }

}