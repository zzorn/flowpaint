package org.flowpaint.model2

import _root_.org.flowpaint.util.Rectangle

/**
 * 
 */
trait Layer {

  /**
   * Render the layer on top of the specified target rastera and data.
   * The target raster and data already contain the combined underlying layer raster and data information.
   */
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap)
}

