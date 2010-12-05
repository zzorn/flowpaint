package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{Picture, DataMap, Raster}

/**
 * 
 */
case class CloneLayer(picture: Picture, source: Layer) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
  
  def channels = source.channels
}
