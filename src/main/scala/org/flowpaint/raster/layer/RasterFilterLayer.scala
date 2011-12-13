package org.flowpaint.raster.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.raster.picture.Picture
import org.flowpaint.raster.channel.Raster

/**
 * Applies a filter function to the whole raster data.
 */
class RasterFilterLayer(val picture: Picture) extends Layer {

  def renderLayer(area: Rectangle, targetRaster: Raster) {
    null
  }

  def channels = Map()
}

