package org.flowpaint.ink

import gradient.Gradient
import util.DataSample
import util.PropertyRegister

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class GradientInk(gradient:Gradient, alphaPressure:Float)  extends Ink {

  def processPixel(pixelData: DataSample)  {

    val positionAlongStroke = pixelData.getProperty( PropertyRegister.POSTION_ALONG_STROKE,0  )
    val positionAcrossStroke = pixelData.getProperty( PropertyRegister.POSITION_ACROSS_STROKE,0  )

    val strokeAlpha = pixelData.getProperty(PropertyRegister.ALPHA, 1f)

    val data : DataSample= gradient( 0.5f + 0.5f * positionAcrossStroke )

    val gradientAlpha: Float = data.getProperty(PropertyRegister.ALPHA, 1)
    val pressure = pixelData.getProperty(PropertyRegister.PRESSURE, 0.5f)
    val alpha = strokeAlpha  * util.MathUtils.interpolate( alphaPressure, gradientAlpha , gradientAlpha * pressure )

    pixelData.setValuesFrom( data )
    pixelData.setProperty( PropertyRegister.ALPHA, alpha )
  }

}