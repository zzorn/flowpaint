package org.flowpaint.model2

import _root_.org.flowpaint.util.Rectangle
import layer.Layer

/**
 * A layer with raster data, rendering the data on top of the provided raster data.
 */
class RasterLayer extends Layer {
  var raster: Raster = new Raster()

  override def channel(name: Symbol) = raster.channels.get(name)

  def channels = raster.channels

  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) {
    targetRaster.overlay(raster, area)
  }

}

