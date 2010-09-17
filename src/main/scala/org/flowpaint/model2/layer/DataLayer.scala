package org.flowpaint.model2

import _root_.org.flowpaint.util.Rectangle


/**
 * Contains located data points, that can be linked to each other to form vector graphics objects.
 * The data itself is not visible, but can be visualized in various ways.
 */
class DataLayer extends Layer {
  var data: DataMap = new DataMap()


  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) {
    targetData.overlay(data, area)
  }
}

