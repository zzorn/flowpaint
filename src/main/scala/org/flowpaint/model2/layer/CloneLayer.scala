package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{DataMap, Raster}

/**
 * 
 */
case class CloneLayer(source: Layer) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
  
  def channels = source.channels
}
