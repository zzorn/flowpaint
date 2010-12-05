package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{Picture, DataMap, Raster}

/**
 * Renders data as strokes and shapes and fills, removing those data objects at the same time.
 */
class VectorLayer(val picture: Picture) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null

  def channels = Map()
}

