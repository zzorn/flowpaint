package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.{DataSample, MathUtils}
/**
 * Calculates pythagoras distance formula.  Useful for circles and radial effects.
 * 
 * @author Hans Haggstrom
 */

class Distance extends PixelProcessor("","","""
    final float x1$id$ = $getScaleOffsetFloat x1, 0f$;
    final float y1$id$ = $getScaleOffsetFloat y1, 0f$;
    final float x2$id$ = $getScaleOffsetFloat x2, 0f$;
    final float y2$id$ = $getScaleOffsetFloat y2, 0f$;

    final float xDiff$id$ = x2$id$ - x1$id$;
    final float yDiff$id$ = y2$id$ - y1$id$;
    final float result$id$ = (float) Math.sqrt( xDiff$id$ * xDiff$id$ + yDiff$id$ * yDiff$id$ );

    $setScaleOffsetFloat result$ result$id$;
  """) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings: Data) {

    val x1 = getScaleOffsetVar( "positionX1", 0f, variables, variableNameMappings )
    val y1 = getScaleOffsetVar( "positionY1", 0f, variables, variableNameMappings )
    val x2 = getScaleOffsetVar( "positionX2", 0f, variables, variableNameMappings )
    val y2 = getScaleOffsetVar( "positionY2", 0f, variables, variableNameMappings )

    val result = MathUtils.distance( x1, y1, x2, y2 )

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }

}