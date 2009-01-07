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

class Add extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val term1 = getScaleOffsetVar( "term1", 0f, variables, variableNameMappings )
    val term2 = getScaleOffsetVar( "term2", 0f, variables, variableNameMappings )
    val term3 = getScaleOffsetVar( "term3", 0f, variables, variableNameMappings )
    val term4 = getScaleOffsetVar( "term4", 0f, variables, variableNameMappings )

    val value = term1 + term2 + term3 + term4

    setScaleOffsetVar( "result", value, variables, variableNameMappings )
  }
}