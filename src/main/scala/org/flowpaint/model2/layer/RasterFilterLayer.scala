package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.Picture
import org.flowpaint.model2.raster.Raster
import org.flowpaint.model2.data.DataMap

/**
 * Applies a filter function to the whole raster data.
 */
class RasterFilterLayer(val picture: Picture) extends Layer {


  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
  def channels = Map()
}

