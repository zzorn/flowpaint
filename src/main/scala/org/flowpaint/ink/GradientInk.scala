package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class GradientInk(gradient:Gradient)  extends Ink {

  def calculateColor(positionAlongStroke: Float, positionAcrossStroke: Float, startData: DataSample, endData: DataSample): Int = {

    val data : DataSample= gradient( 0.5f + 0.5f * positionAcrossStroke )

    util.ColorUtils.createRGBAColor( data.getProperty("red",0 ),
      data.getProperty("green",0 ),
      data.getProperty("blue",0 ),
      data.getProperty("alpha",1 ))
  }

}