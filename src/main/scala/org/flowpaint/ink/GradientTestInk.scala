package org.flowpaint.brush

import ink.Ink
import util.DataSample

/**
 *   Just a simple test brush.
 *
 * @author Hans Haggstrom
 */
class GradientTestInk extends Ink {
  def calculateColor(positionAlongStroke: Float, positionAcrossStroke: Float,
                    startData: DataSample, endData: DataSample): Int = {


    def getInterpolatedProperty( name:String, default:Float ) :Float = {
        util.MathUtils.interpolate(
      positionAlongStroke,
      startData.getProperty(name, default),
      endData.getProperty(name, default))
    }

    val red = 1f
    val blue = 0f
    val green = (0.5 + 0.5 * Math.sin(getInterpolatedProperty("time",0)*20)).toFloat

/*
    val red = getInterpolatedProperty("red",0)
    val green = getInterpolatedProperty("green",0)
    val blue = getInterpolatedProperty("blue",0)
*/

    val alpha = 1f - positionAcrossStroke

    val r = (255 * red).toInt
    val g = (255 * green).toInt
    val b = (255 * blue).toInt
    val a = (255 * alpha).toInt

    val color = ((a & 0xFF) << 24) |
            ((r & 0xFF) << 16) |
            ((g & 0xFF) << 8) |
            ((b & 0xFF) << 0);

    color
  }
}
