package org.flowpaint.model2.raster

import tile.SingleValueTile

/**
 * 
 */
object TileService {
  val tileWidth = 64
  val tileHeight = 64

  def tileIdForLocation(canvasX: Int, canvasY: Int): TileId = {
    // Integer division is truncated towards zero, so if the canvas coordinates are less
    // than zero we subtract one to ensure the negative tile indexes start with -1.
    val tileX = canvasX / tileWidth - (if (canvasX < 0) 1 else 0)
    val tileY = canvasY / tileHeight  - (if (canvasY < 0) 1 else 0)

    // Optimize: We could cache the TileId:s maybe, but they are pretty light.
    return TileId(tileX, tileY)
  }

  // TODO: Implement tile pooling
  def allocateDataTile(): DataTile = new DataTile()
  def freeDataTile(tile: DataTile) = {}

  def allocateDataTile(sourceTile: Tile): DataTile = {
    val newTile = allocateDataTile()

    sourceTile match {
      case dataTile: DataTile =>
        Array.copy(dataTile.data, 0, newTile.data, 0, dataTile.data.length)
      case valueTile: SingleValueTile =>
        val v = valueTile.value
        var i = newTile.data.length - 1
        while (i >= 0) {
          newTile.data(i) = v
          i -= 1
        }
    }

    newTile
  }
}