package org.flowpaint.model2.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.model2.{Picture}
import org.flowpaint.model2.data.DataMap
import org.flowpaint.model2.raster.Raster

/**
 * 
 */
case class CloneLayer(picture: Picture, source: Layer) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap) = null
  
  def channels = source.channels
}
