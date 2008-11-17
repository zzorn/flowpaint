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

    /*
        val r = 1
        val g = 1 - (1 - positionAlongStroke) * normalizedCenter
        val b = 1 - normalizedCenter
    */
    /*
        val red = (255 * r).toInt
        val green = (255 * g).toInt
        val blue = (255 * b).toInt
    */


    val red = util.MathUtils.interpolate(
      positionAlongStroke,
      startData.getProperty("red", 0),
      endData.getProperty("red", 0))

    val green = util.MathUtils.interpolate(
      positionAlongStroke,
      startData.getProperty("green", 0),
      endData.getProperty("green", 0))

    val blue = util.MathUtils.interpolate(
      positionAlongStroke,
      startData.getProperty("blue", 0),
      endData.getProperty("blue", 0))

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
