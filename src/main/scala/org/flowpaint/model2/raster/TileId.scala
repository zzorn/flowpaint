package org.flowpaint.model2.raster

/**
 * Identifier for a cell at the specified cell indexes.
 */
case class TileId(tileX: Int, tileY: Int)

object TileId {

  def forLocation(canvasX: Int, canvasY: Int): TileId = {
    // Integer division is truncated towards zero, so if the canvas coordinates are less
    // than zero we subtract one to ensure the negative tile indexes start with -1.
    val tileX = canvasX / TileService.tileWidth - (if (canvasX < 0) 1 else 0)
    val tileY = canvasY / TileService.tileHeight  - (if (canvasY < 0) 1 else 0)

    // Optimize: We could cache the TileId:s maybe, but they are pretty light.
    return TileId(tileX, tileY)
  }

}