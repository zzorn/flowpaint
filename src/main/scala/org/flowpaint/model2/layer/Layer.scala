package org.flowpaint.model2.layer

import org.flowpaint.model2.{DataMap, Raster}
import org.flowpaint.util.Rectangle
import org.flowpaint.model2.raster.{TileId, Channel}


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

  def channels: Map[Symbol, Channel]

  /**
   * Render the layer on top of the specified target raster and data.
   * The target raster and data already contain the combined underlying layer raster and data information.
   * Only the area falling within the specified area need to be rendered.
   */
  def renderLayer(area: Rectangle, targetRaster: Raster, targetData: DataMap)

  /**
   * Retrieves the id:s of the tiles that need to be redrawn.
   */
  def getDirtyTiles(): Set[TileId] = {
    var dirty: Set[TileId] = Set()
    channels.values foreach (c => dirty ++= c.dirtyTileIds)
    dirty
  }

  /**
   * Clears the dirty status of the tiles in the layer.
   */
  def clearDirtyTiles() {
    channels.values foreach (_.cleanDirtyTiles())
  }

}

