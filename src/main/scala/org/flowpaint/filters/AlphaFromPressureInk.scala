package org.flowpaint.filters
import ink.Ink
import util.{DataSample, PropertyRegister}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class AlphaFromPressureInk(pressureEffect:Float) extends Ink {
  def processPixel(pixelData: DataSample) = {
    val pressure = pixelData.getProperty( PropertyRegister.PRESSURE, 0.5f )
    val oldAlpha = pixelData.getProperty( PropertyRegister.ALPHA, 1 )

    val alpha = util.MathUtils.interpolate( pressureEffect, oldAlpha, pressure * oldAlpha )

    pixelData.setProperty( PropertyRegister.ALPHA, alpha )
    
  }

}