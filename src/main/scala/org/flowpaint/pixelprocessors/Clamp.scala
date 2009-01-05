package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Clamp extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getMappedVar( "value", 0f, variables, variableNameMappings )
    val min = getMappedVar( "min", 0f, variables, variableNameMappings )
    val max = getMappedVar( "max", 1f, variables, variableNameMappings )

    val result = if ( value < min ) min
                 else if (value > max) max
                 else value

    setMappedVar( "result", result, variables, variableNameMappings )
  }
}