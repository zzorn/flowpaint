package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{Picture, DataMap, Raster}

/**
 * Applies a filter function to the whole raster data.
 */
class RasterFilterLayer(val picture: Picture) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) {
    // TODO
  }


  def channels = Map()
}

