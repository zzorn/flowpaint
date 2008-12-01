package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

/**
 * Multiplies existing color with the color derived from the hue, sat, brightness -properties,
 * or if no existing color is available, uses the color directly.
 * 
 * @author Hans Haggstrom
 */
class ColorInk() extends Ink {

  def processPixel(pixelData: DataSample) {


    val hue = pixelData.getProperty( "hue", 0 )
    val saturation = pixelData.getProperty( "saturation", 0 )
    val brightness = pixelData.getProperty( "brightness", 0 )

    val color = java.awt.Color.getHSBColor( hue, saturation, brightness )

    val r = pixelData.getProperty("red",1) * color.getRed / 255f
    val g = pixelData.getProperty("green",1) * color.getGreen / 255f
    val b = pixelData.getProperty("blue",1) * color.getBlue / 255f
    val a = pixelData.getProperty("alpha",1) * color.getAlpha / 255f

    pixelData.setProperty( "red", r )
    pixelData.setProperty( "green", g)
    pixelData.setProperty( "blue", b )
    pixelData.setProperty( "alpha", a )
  }


}