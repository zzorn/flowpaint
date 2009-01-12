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

class Distance extends PixelProcessor("","","") {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings: Data) {

    val x1 = getScaleOffsetVar( "positionX1", 0f, variables, variableNameMappings )
    val y1 = getScaleOffsetVar( "positionY1", 0f, variables, variableNameMappings )
    val x2 = getScaleOffsetVar( "positionX2", 0f, variables, variableNameMappings )
    val y2 = getScaleOffsetVar( "positionY2", 0f, variables, variableNameMappings )

    val result = MathUtils.distance( x1, y1, x2, y2 )

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }

}