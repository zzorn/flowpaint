package org.flowpaint.model2

import org.flowpaint.util.Rectangle
import raster.Channel

/**
 * 
 */
final class RgbaRasterRenderer(renderAlphaAsCheckers: Boolean = true, alphaGridSize: Int = 16,
                               alphaLuminance1 : Float = 0.5f, alphaLuminance2: Float = 0.7f) extends RasterRenderer {

  val redChannel   = 'red
  val greenChannel = 'green
  val blueChannel  = 'blue
  val alphaChannel = 'alpha

  def render(source: Raster, sourceArea: Rectangle, 
             targetArea: Rectangle,
             target: Array[Int], targetWidth: Int, targetHeight: Int) {

    // This algorithm isn't really a very optimal way to do this,
    // but its a bit hard to optimize due to the the block based design (which is needed for memory optimization reasons)

    val red: Channel = source.channels(redChannel)
    val green: Channel = source.channels(greenChannel)
    val blue: Channel = source.channels(blueChannel)
    val alpha: Channel = source.channels(alphaChannel)

    val endX = targetArea.x2
    var x = 0
    var i = 0
    var y = targetArea.y1
    while (y < targetArea.y2) {
      x = targetArea.x1
      i = targetArea.y1 * targetWidth + targetArea.x1

      if (renderAlphaAsCheckers)
        // Render alpha value as a solid checkerboard pattern behind visible content
        while (x < endX) {

          val a = alpha.getValueAt(x, y)
          if (a >= 1f) {
            target(i) = 0xFF000000 | (red.getByteValueAt(x, y) << 16) | (green.getByteValueAt(x,y) << 8) | blue.getByteValueAt(x,y)
          }
          else {
            // Calculate checkerboard alpha pattern if we have some transparency
            val alphaLuminance = if (! ((x % alphaGridSize * 2 < alphaGridSize) == (y % alphaGridSize * 2 < alphaGridSize))) alphaLuminance1 else alphaLuminance2
            val preMultipliedAlphaLuminance = (1f - a) * alphaLuminance

            val r = (255 * (preMultipliedAlphaLuminance + red(index)   * a)).toInt
            val g = (255 * (preMultipliedAlphaLuminance + green(index) * a)).toInt
            val b = (255 * (preMultipliedAlphaLuminance + blue(index)  * a)).toInt

            target(i) = 0xFF000000 | (r << 16) | (g << 8) | b
          }

          i += 1
          x += 1
        }
      else
        // Render alpha value into highest byte
        while (x < endX) {
          target(i) = (alpha.getByteValue(x,y) << 24) |
                      (red.getByteValue(x,y)   << 16) |
                      (green.getByteValue(x,y) <<  8) |
                      blue.getByteValue(x,y)

          i += 1
          x += 1
        }

      y += 1
    }
  }


}

