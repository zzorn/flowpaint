package org.flowpaint.pixelprocessors
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.{DataSample, MathUtils}
/**
 * 
 *
 * @author Hans Haggstrom
 */

class InterpolateSmoothly extends PixelProcessor("",
  """
      
  """ ) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val input = getScaleOffsetVar( "input", 0.5f, variables, variableNameMappings )
    val inputStart = getScaleOffsetVar( "inputStart", 0f, variables, variableNameMappings )
    val inputEnd = getScaleOffsetVar( "inputEnd", 1f, variables, variableNameMappings )
    val outputStart = getScaleOffsetVar( "outputStart", 0f, variables, variableNameMappings )
    val outputEnd = getScaleOffsetVar( "outputEnd", 1f, variables, variableNameMappings )

    val result = MathUtils.interpolateSmoothly( input, inputStart, inputEnd, outputStart, outputEnd )

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }
}