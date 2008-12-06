package org.flowpaint.ink

import gradient.Gradient
import util.DataSample
import util.PropertyRegister

/**
 * Multiplies existing color with the color derived from the hue, sat, lightness -properties,
 * or if no existing color is available, uses the color directly.
 * 
 * @author Hans Haggstrom
 */
class ColorInk() extends Ink {

  def processPixel(pixelData: DataSample) {


    val hue = pixelData.getProperty( PropertyRegister.HUE, 0 )
    val saturation = pixelData.getProperty( PropertyRegister.SATURATION, 0 )
    val brightness = pixelData.getProperty( PropertyRegister.LIGHTNESS, 0 )

    val (red, green, blue )= util.ColorUtils.HSLtoRGB( hue, saturation, brightness )

    val r = pixelData.getProperty(PropertyRegister.RED,1) * red
    val g = pixelData.getProperty(PropertyRegister.GREEN,1) * green
    val b = pixelData.getProperty(PropertyRegister.BLUE,1) * blue

    pixelData.setProperty( PropertyRegister.RED, r )
    pixelData.setProperty( PropertyRegister.GREEN, g)
    pixelData.setProperty( PropertyRegister.BLUE, b )
  }


}