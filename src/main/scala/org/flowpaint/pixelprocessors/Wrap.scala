package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import pixelprocessor.PixelProcessor
import _root_.scala.collection.Map
import util.{DataSample, MathUtils}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Wrap extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getMappedVar( "value", 0f, variables, variableNameMappings )
    val wrapMax = getMappedVar( "wrapMax", 1f, variables, variableNameMappings )
    val inputScale = getMappedVar( "inputScale", 1f, variables, variableNameMappings )
    val inputOffset = getMappedVar( "inputOffset", 0f, variables, variableNameMappings )
    val outputScale = getMappedVar( "outputScale", 1f, variables, variableNameMappings )
    val outputOffset = getMappedVar( "outputOffset", 0f, variables, variableNameMappings )

    val result = MathUtils.wrap( value * inputScale + inputOffset, wrapMax ) * outputScale + outputOffset

    setMappedVar( "result", result, variables, variableNameMappings )
  }
}