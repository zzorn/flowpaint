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

class Multiply extends PixelProcessor("","","""
    final float value1$id$ = $getScaleOffsetFloat value1, 1f$;
    final float value2$id$ = $getScaleOffsetFloat value2, 1f$;
    final float value3$id$ = $getScaleOffsetFloat value3, 1f$;
    final float value4$id$ = $getScaleOffsetFloat value4, 1f$;
    final float value5$id$ = $getScaleOffsetFloat value5, 1f$;

    final float result$id$ = value1$id$ * value2$id$ * value3$id$ * value4$id$ * value5$id$;

    $setScaleOffsetFloat result$ result$id$;
  """) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val factor1 = getScaleOffsetVar( "factor1", 1f, variables, variableNameMappings )
    val factor2 = getScaleOffsetVar( "factor2", 1f, variables, variableNameMappings )
    val factor3 = getScaleOffsetVar( "factor3", 1f, variables, variableNameMappings )
    val factor4 = getScaleOffsetVar( "factor4", 1f, variables, variableNameMappings )

    val value = factor1 * factor2 * factor3 * factor4

    setScaleOffsetVar( "result", value, variables, variableNameMappings )
  }
}