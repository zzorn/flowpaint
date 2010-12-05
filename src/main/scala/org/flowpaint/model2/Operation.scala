package org.flowpaint.model2

import layer.Layer
import raster.{DataTile, TileId}

/**
 * Some operation that can be applied to a picture.
 * Can be run in a separate worker thread, so should not have external shared dependencies, and should be thread safe.
 */
trait Operation {

  /**
   * Description of the operation, for undo stack UI etc.
   */
  def description: String

  /**
   * Returns the id:s of the tiles that the operation will change.
   */
  def affectedTiles(picture: Picture, layer: Layer): Set[TileId]

  /**
   * The ID:s of the channels that are changed by this operation.
   */
  def affectedChannels(picture: Picture, layer: Layer): Set[Symbol]

  /**
   * Apply the operation to the affected channels of the specific tile of the specified picture.
   * @param tiles a map from the channel identifier to the modifiable data tile for that channel.
   */
  def renderToTile(picture: Picture, layer: Layer, tileId: TileId, tiles: Map[Symbol, DataTile])

}