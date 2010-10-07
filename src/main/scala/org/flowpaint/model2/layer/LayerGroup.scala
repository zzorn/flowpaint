package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{DataMap, Raster, Layer}

/**
 * A group of layers, rendered as one.
 */

class LayerGroup extends Layer{
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
}

