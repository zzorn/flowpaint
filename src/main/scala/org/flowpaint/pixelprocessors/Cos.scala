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

class Cos extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getScaleOffsetVar( "value", 0f, variables, variableNameMappings )

    val result = Math.cos( value ).toFloat 

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }
}