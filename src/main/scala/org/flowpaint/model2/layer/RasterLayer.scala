package org.flowpaint.model2

import _root_.org.flowpaint.util.Rectangle
import layer.Layer

/**
 * A layer wit raster data, rendering the data on top of the provided raster data.
 */
class RasterLayer extends Layer {
  var raster: Raster = new Raster()

  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) {
    targetRaster.overlay(raster, area, null)
  }
}

