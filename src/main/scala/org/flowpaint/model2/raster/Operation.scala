package org.flowpaint.model2.raster

/**
 * 
 */
trait Operation {

  /**
   * Return the ids of the tiles affected by this operation for the specified channel.
   */
  def affectedTiles(channel: Symbol): Set[TileId]

  /**
   * Runs this operation for a single tile.
   */
  def processTile(channel: Symbol, tileId: TileId, tile: DataTile)

}