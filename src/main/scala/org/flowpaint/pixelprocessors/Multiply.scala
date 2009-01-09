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

class Multiply extends PixelProcessor("","") {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val factor1 = getScaleOffsetVar( "factor1", 1f, variables, variableNameMappings )
    val factor2 = getScaleOffsetVar( "factor2", 1f, variables, variableNameMappings )
    val factor3 = getScaleOffsetVar( "factor3", 1f, variables, variableNameMappings )
    val factor4 = getScaleOffsetVar( "factor4", 1f, variables, variableNameMappings )

    val value = factor1 * factor2 * factor3 * factor4

    setScaleOffsetVar( "result", value, variables, variableNameMappings )
  }
}