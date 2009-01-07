package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.DataSample
import util.MathUtils._
import pixelprocessor.PixelProcessor


/**
 * 
 * 
 * @author Hans Haggstrom
 */

class RBGToHSL extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) = {

    val red = clampToZeroToOne( getScaleOffsetVar ( "red", 0, variables, variableNameMappings ) )
    val green = clampToZeroToOne( getScaleOffsetVar ( "green", 0, variables, variableNameMappings ) )
    val blue = clampToZeroToOne( getScaleOffsetVar ( "blue", 0, variables, variableNameMappings ) )

    val (hue, saturation, lightness )= util.ColorUtils.RGBtoHSL( red, green, blue )

    setScaleOffsetVar( "hue", hue, variables,variableNameMappings )
    setScaleOffsetVar( "saturation", saturation, variables,variableNameMappings )
    setScaleOffsetVar( "lightness", lightness, variables,variableNameMappings )
  }

}