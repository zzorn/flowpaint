package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class GradientInk(gradient:Gradient, alphaPressure:Float)  extends Ink {

  def calculateColor(positionAlongStroke: Float, positionAcrossStroke: Float, startData: DataSample, endData: DataSample): Int = {

    def getInterpolatedProperty( name:String, default:Float ) :Float = {
        util.MathUtils.interpolate(
      positionAlongStroke,
      startData.getProperty(name, default),
      endData.getProperty(name, default))
    }

    val data : DataSample= gradient( 0.5f + 0.5f * positionAcrossStroke )

    val gradientAlpha: Float = data.getProperty("alpha", 1)
    val pressure = getInterpolatedProperty("pressure", 0.5f)
    val alpha = util.MathUtils.interpolate( alphaPressure, gradientAlpha , gradientAlpha * pressure )

    util.ColorUtils.createRGBAColor( data.getProperty("red",0 ),
      data.getProperty("green",0 ),
      data.getProperty("blue",0 ),
      alpha)
  }

}