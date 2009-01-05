package org.flowpaint.pixelprocessors

import _root_.scala.collection.Map
import util.DataSample
import util.MathUtils._


/**
 * 
 * 
 * @author Hans Haggstrom
 */

class RBGToHSL extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String]) = {

    val red = clampToZeroToOne( getMappedVar ( "red", 0, variables, variableNameMappings ) )
    val green = clampToZeroToOne( getMappedVar ( "green", 0, variables, variableNameMappings ) )
    val blue = clampToZeroToOne( getMappedVar ( "blue", 0, variables, variableNameMappings ) )

    val (hue, saturation, lightness )= util.ColorUtils.RGBtoHSL( red, green, blue )

    setMappedVar( "hue", hue, variables,variableNameMappings )
    setMappedVar( "saturation", saturation, variables,variableNameMappings )
    setMappedVar( "lightness", lightness, variables,variableNameMappings )
  }

}