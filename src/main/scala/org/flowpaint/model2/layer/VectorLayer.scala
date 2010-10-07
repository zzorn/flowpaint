package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{DataMap, Raster}

/**
 * Renders data as strokes and shapes and fills, removing those data objects at the same time.
 */
class VectorLayer extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
}

