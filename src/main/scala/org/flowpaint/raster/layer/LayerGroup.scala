package org.flowpaint.raster.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.raster.picture.Picture
import org.flowpaint.raster.channel.Raster

/**
 * A group of layers, rendered as one.
 */

class LayerGroup(val picture: Picture) extends Layer  {
  def renderLayer(area: Rectangle, targetRaster: Raster) {
    null
  }

  def channels = Map()

  // TODO: Override getdirty tiles etc
}

