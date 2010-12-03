package org.flowpaint.model2.layer

import org.flowpaint.model2.raster.Channel
import org.flowpaint.model2.{DataMap, Raster}
import org.flowpaint.util.Rectangle


/**
 * 
 */
trait Layer {

  type LayerListener = () => Unit

  private var listeners: Set[LayerListener] = Set()
  private var _identifier: Symbol = 'layer

  def identifier(): Symbol = _identifier
  def setIdentifier(id: Symbol) {
    require(id != null)
    _identifier = id
    onLayerChanged()
  }

  def addListener(listener: LayerListener) = listeners += listener
  def removeListener(listener: LayerListener) = listeners -= listener

  protected def onLayerChanged() = listeners foreach (_())

  def channel(name: Symbol): Option[Channel] = None

  /**
   * Render the layer on top of the specified target raster and data.
   * The target raster and data already contain the combined underlying layer raster and data information.
   * Only the area falling within the specified area need to be rendered.
   */
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap)
}

