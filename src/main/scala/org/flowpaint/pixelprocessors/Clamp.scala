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

class Clamp extends PixelProcessor("","","""
    final float value$id$ = $getScaleOffsetFloat value, 0f$;
    final float min$id$ = $getScaleOffsetFloat min, 0f$;
    final float max$id$  = $getScaleOffsetFloat max, 1f$;

    final float result$id$ = value$id$ > max$id$ ? max$id$ : ( value$id$ < min$id$ ? min$id$ : value$id$);

    $setScaleOffsetFloat result$ result$id$;
  """) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val value = getScaleOffsetVar( "value", 0f, variables, variableNameMappings )
    val min = getScaleOffsetVar( "min", 0f, variables, variableNameMappings )
    val max = getScaleOffsetVar( "max", 1f, variables, variableNameMappings )

    val result = if ( value < min ) min
                 else if (value > max) max
                 else value

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }
}