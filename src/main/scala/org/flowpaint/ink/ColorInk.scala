package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class ColorInk( val gradient : Gradient ) extends Ink {

  def processPixel(pixelData: DataSample) {


    val hue = pixelData.getProperty( "hue", 0 )
    val saturation = pixelData.getProperty( "saturation", 0 )
    val brightness = pixelData.getProperty( "brightness", 0 )

    val color = java.awt.Color.getHSBColor( hue, saturation, brightness )

    val positionAcrossStroke = pixelData.getProperty( "positionAcrossStroke",0  )
    val gradientPoint : DataSample= gradient( 0.5f + 0.5f * positionAcrossStroke )

    val r = gradientPoint.getProperty("red",1) * color.getRed / 255f
    val g = gradientPoint.getProperty("green",1) * color.getGreen / 255f
    val b = gradientPoint.getProperty("blue",1) * color.getBlue / 255f
    val a = gradientPoint.getProperty("alpha",1) * (color.getAlpha / 255f) * pixelData.getProperty("alpha",1)

    pixelData.setProperty( "red", r )
    pixelData.setProperty( "green", g)
    pixelData.setProperty( "blue", b )
    pixelData.setProperty( "alpha", a )
  }


}