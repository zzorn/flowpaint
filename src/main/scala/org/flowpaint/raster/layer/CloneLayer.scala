package org.flowpaint.raster.layer

import org.flowpaint.util.Rectangle
import org.flowpaint.raster.picture.Picture
import org.flowpaint.raster.channel.Raster

/**
 * 
 */
case class CloneLayer(picture: Picture, source: Layer) extends Layer {
  def renderLayer(area: Rectangle, targetRaster: Raster/*, targetData: DataMap*/) = null
  
  def channels = source.channels
}
