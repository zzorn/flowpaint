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

class Sin extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getMappedVar( "value", 0f, variables, variableNameMappings )
    val inputScale = getMappedVar( "inputScale", 1f, variables, variableNameMappings )
    val inputOffset = getMappedVar( "inputOffset", 0f, variables, variableNameMappings )
    val outputScale = getMappedVar( "outputScale", 1f, variables, variableNameMappings )
    val outputOffset = getMappedVar( "outputOffset", 0f, variables, variableNameMappings )

    val result = Math.sin( value * inputScale + inputOffset ).toFloat * outputScale + outputOffset

    setMappedVar( "result", result, variables, variableNameMappings )
  }
}