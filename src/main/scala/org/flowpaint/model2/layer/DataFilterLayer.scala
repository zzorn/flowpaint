package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{DataMap, Raster}

/**
 * Applies a filter to select data objects.
 */
class DataFilterLayer extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null

  def channels = Map()
}

