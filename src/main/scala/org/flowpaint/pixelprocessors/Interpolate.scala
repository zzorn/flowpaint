package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.{DataSample, MathUtils}
import pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Interpolate extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val input = getMappedVar( "input", 0.5f, variables, variableNameMappings )
    val inputStart = getMappedVar( "inputStart", 0f, variables, variableNameMappings )
    val inputEnd = getMappedVar( "inputEnd", 1f, variables, variableNameMappings )
    val outputStart = getMappedVar( "outputStart", 0f, variables, variableNameMappings )
    val outputEnd = getMappedVar( "outputEnd", 1f, variables, variableNameMappings )

    val result = MathUtils.interpolate( input, inputStart, inputEnd, outputStart, outputEnd )

    setMappedVar( "result", result, variables, variableNameMappings )
  }
}