package org.flowpaint.ink

import gradient.Gradient
import util.DataSample
import util.PropertyRegister

/**
 * Multiplies existing color with the color derived from the hue, sat, brightness -properties,
 * or if no existing color is available, uses the color directly.
 * 
 * @author Hans Haggstrom
 */
class ColorInk() extends Ink {

  def processPixel(pixelData: DataSample) {


    val hue = pixelData.getProperty( PropertyRegister.HUE, 0 )
    val saturation = pixelData.getProperty( PropertyRegister.SATURATION, 0 )
    val brightness = pixelData.getProperty( PropertyRegister.BRIGHTNESS, 0 )

    val color = java.awt.Color.getHSBColor( hue, saturation, brightness )

    val r = pixelData.getProperty(PropertyRegister.RED,1) * color.getRed / 255f
    val g = pixelData.getProperty(PropertyRegister.GREEN,1) * color.getGreen / 255f
    val b = pixelData.getProperty(PropertyRegister.BLUE,1) * color.getBlue / 255f
    val a = pixelData.getProperty(PropertyRegister.ALPHA,1) * color.getAlpha / 255f

    pixelData.setProperty( PropertyRegister.RED, r )
    pixelData.setProperty( PropertyRegister.GREEN, g)
    pixelData.setProperty( PropertyRegister.BLUE, b )
    pixelData.setProperty( PropertyRegister.ALPHA, a )
  }


}