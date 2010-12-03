package org.flowpaint.model2

import layer.Layer
import raster.TileId

/**
 * 
 */
class Picture {

  type PictureListener = Picture => Unit

  private var listeners: List[PictureListener] = Nil
  private var _layers: List[Layer] = Nil

  def layer(name: Symbol): Option[Layer] = layers.find(_.identifier == name)

  def layers: List[Layer] = _layers

  def addLayer(layer: Layer) {
    require(layer != null)
    require(!_layers.contains(layer))

    _layers = _layers ::: List(layer)



    onPictureChanged()
  }

  def removeLayer(layer: Layer){
    require(layer != null)
    require(_layers.contains(layer))

    _layers -= layer

    onPictureChanged()
  }
  
  def addListener(listener: PictureListener) = listeners ::= listener
  def removeListener(listener: PictureListener) = listeners -= listener

  private def onPictureChanged() = listeners foreach (_(this))

  /**
   * Retrieves the id:s of the tiles that need to be redrawn, and clears the dirty status at the same time.
   */
  def getAndClearDirtyTiles(): Set[TileId]

  def hasDirtyTiles(): Boolean

}

