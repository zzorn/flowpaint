package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{Picture}
import org.flowpaint.model2.raster.Raster
import org.flowpaint.model2.data.DataMap

/**
 * A group of layers, rendered as one.
 */

class LayerGroup(val picture: Picture) extends Layer  {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
  def channels = Map()

  // TODO: Override getdirty tiles etc
}

