package org.flowpaint.model2

import layer.Layer
import org.flowpaint.util.CommandQueue
import raster.{Changes, Change, TileId}

/**
 * 
 */
class Picture {

  type PictureListener = Picture => Unit

  private var listeners: List[PictureListener] = Nil
  private var _layers: List[Layer] = Nil
  private var _currentLayer: Layer = null

  def currentLayer: Layer = _currentLayer
  def setCurrentLayer(layer: Layer) {
    require(layer == null || _layers.contains(layer))
    _currentLayer = layer
  }


  val commandQueue = new CommandQueue[Picture](this)

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

  def onPictureChanged() = listeners foreach (_(this))

  /**
   * Retrieves the id:s of the tiles that need to be redrawn, and clears the dirty status at the same time.
   */
  def getAndClearDirtyTiles(): Set[TileId] = {
    val tiles = getDirtyTiles()
    clearDirtyTiles
    tiles
  }

  /**
   * Retrieves the id:s of the tiles that need to be redrawn.
   */
  def getDirtyTiles(): Set[TileId] = {
    var dirty: Set[TileId] = Set()
    _layers foreach (l => dirty ++= l.getDirtyTiles())
    dirty
  }

  /**
   * Clears the dirty status of the tiles in the picture.
   */
  def clearDirtyTiles() {
    _layers foreach (_.clearDirtyTiles())
  }

  def takeUndoSnapshot(): Change = {
    Changes(_layers.map(_.takeUndoSnapshot()))
  }

  def hasDirtyTiles(): Boolean = !getDirtyTiles().isEmpty

}

