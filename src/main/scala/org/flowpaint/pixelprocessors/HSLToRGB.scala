package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.{DataSample, MathUtils}
import util.MathUtils._
import pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class HSLToRGB  extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) = {

    val hue = wrapToZeroToOne( getMappedVar ( "hue", 0, variables, variableNameMappings ) )
    val saturation = clampToZeroToOne( getMappedVar ( "saturation", 0, variables, variableNameMappings ) )
    val lightness  = clampToZeroToOne( getMappedVar ( "lightness", 0, variables, variableNameMappings ) )

    val (red, green, blue )= util.ColorUtils.HSLtoRGB( hue, saturation, lightness )

    setMappedVar( "red", red, variables,variableNameMappings )
    setMappedVar( "green", green, variables,variableNameMappings )
    setMappedVar( "blue", blue, variables,variableNameMappings )
  }

}