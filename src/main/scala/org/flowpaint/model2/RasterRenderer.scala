package org.flowpaint.model2

import _root_.java.awt.image.BufferedImage
import java.awt.Image
import org.flowpaint.util.Rectangle

/**
 * Renders a raster to an int array of packed rgba pixels, allowing it to be displayed or saved in simple image formats.
 */
trait RasterRenderer {
  def render(source: Raster, sourceArea: Rectangle, targetArea: Rectangle,
             target: Array[Int], targetWidth: Int, targetHeight: Int)
}

