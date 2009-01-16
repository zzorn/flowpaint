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

class Add extends PixelProcessor("","",  """
    final float value1$id$ = $getScaleOffsetFloat value1, 0f$;
    final float value2$id$ = $getScaleOffsetFloat value2, 0f$;
    final float value3$id$ = $getScaleOffsetFloat value3, 0f$;
    final float value4$id$ = $getScaleOffsetFloat value4, 0f$;
    final float value5$id$ = $getScaleOffsetFloat value5, 0f$;

    final float sum$id$ = value1$id$ + value2$id$ + value3$id$ + value4$id$ + value5$id$;

    $setScaleOffsetFloat result$ sum$id$;
  """) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val term1 = getScaleOffsetVar( "term1", 0f, variables, variableNameMappings )
    val term2 = getScaleOffsetVar( "term2", 0f, variables, variableNameMappings )
    val term3 = getScaleOffsetVar( "term3", 0f, variables, variableNameMappings )
    val term4 = getScaleOffsetVar( "term4", 0f, variables, variableNameMappings )

    val value = term1 + term2 + term3 + term4

    setScaleOffsetVar( "result", value, variables, variableNameMappings )
  }
}