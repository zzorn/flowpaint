package org.flowpaint.model2

import _root_.java.awt.image.BufferedImage
import _root_.org.flowpaint.util.Rectangle

/**
 * 
 */
class RasterRendererImpl extends RasterRenderer {

  val redChannel   = 'red
  val greenChannel = 'green
  val blueChannel  = 'blue
  val alphaChannel = 'alpha

  def render(raster: Raster, area: Rectangle, image: BufferedImage) {
    val x = area.x1
    val y = area.y1
    val scaleX = image.getWidth / area.width
    val scaleY = image.getHeight / area.height

    // TODO: Clear image to full transparent

    val blocks = raster.getBlocks(area)
    blocks.foreach( b => b.render(image, x, y, scaleX, scaleY, redChannel, greenChannel, blueChannel, alphaChannel) )
  }
}

