package org.flowpaint.model2.layer

import org.flowpaint.model2.raster.Channel
import org.flowpaint.model2.{DataMap, Raster}
import org.flowpaint.util.Rectangle


/**
 * 
 */
trait Layer {

  var identifier: Symbol = null
  
  def channel(name: Symbol): Option[Channel] = None

  /**
   * Render the layer on top of the specified target raster and data.
   * The target raster and data already contain the combined underlying layer raster and data information.
   * Only the area falling within the specified area need to be rendered.
   */
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap)
}

