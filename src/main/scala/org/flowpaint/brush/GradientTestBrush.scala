package org.flowpaint.brush

/**
 *  Just a simple test brush.
 *
 * @author Hans Haggstrom
 */
class GradientTestBrush extends Brush {

  def calculateColor(positionAlongStroke: Float, positionAcrossStroke: Float): Int = {
    val normalizedCenter = 1f - positionAcrossStroke

    val r = 1
    val g = 1 - (1 - positionAlongStroke) * normalizedCenter
    val b = 1 - normalizedCenter

    val red = (255 * r).toInt
    val green = (255 * g).toInt
    val blue = (255 * b).toInt
    val alpha = 255

    val color = ((alpha & 0xFF) << 24) |
            ((red & 0xFF) << 16) |
            ((green & 0xFF) << 8) |
            ((blue & 0xFF) << 0);

    color
  }
}
