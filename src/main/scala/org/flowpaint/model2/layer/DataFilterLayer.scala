package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{Picture, DataMap, Raster}

/**
 * Applies a filter to select data objects.
 */
class DataFilterLayer(val picture: Picture) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null

  def channels = Map()
}

