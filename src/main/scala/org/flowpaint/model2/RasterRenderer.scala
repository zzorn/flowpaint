package org.flowpaint.model2

import _root_.java.awt.image.BufferedImage
import java.awt.Image
import org.flowpaint.util.Rectangle

/**
 * Renders a raster to a java image, allowing it to be displayed or saved in simple image formats.
 */
trait RasterRenderer {
  def render(raster: Raster, area: Rectangle, image: BufferedImage)
}

