package org.flowpaint.pixelprocessor
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

class SingleFunctionPixelProcessor( function : (Double) => Double ) extends PixelProcessor {


  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getScaleOffsetVar( "value", 0f, variables, variableNameMappings )

    val result = function( value ).toFloat

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }


}