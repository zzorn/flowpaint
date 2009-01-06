package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.DataSample
import pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Signum extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getScaleOffsetVar( "value", 0f, variables, variableNameMappings )

    val result = Math.signum( value ).toFloat 

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }
}