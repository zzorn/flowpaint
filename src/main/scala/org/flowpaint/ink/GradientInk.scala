package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class GradientInk(gradient:Gradient, alphaPressure:Float)  extends Ink {

  def processPixel(pixelData: DataSample)  {

    val positionAlongStroke = pixelData.getProperty( "positionAlongStroke",0  )
    val positionAcrossStroke = pixelData.getProperty( "positionAcrossStroke",0  )

    val strokeAlpha = pixelData.getProperty("alpha", 1f)

    val data : DataSample= gradient( 0.5f + 0.5f * positionAcrossStroke )

    val gradientAlpha: Float = data.getProperty("alpha", 1)
    val pressure = pixelData.getProperty("pressure", 0.5f)
    val alpha = strokeAlpha  * util.MathUtils.interpolate( alphaPressure, gradientAlpha , gradientAlpha * pressure )

    pixelData.setValuesFrom( data )
    pixelData.setProperty( "alpha", alpha )
  }

}