package org.flowpaint.raster

import org.flowpaint.util.Rectangle

/**
 * Identifier for a cell at the specified cell indexes.
 */
final case class TileId(tileX: Int, tileY: Int) {
  def intersects(area: Rectangle): Boolean = {
    area.intersects(tileX * TileService.tileWidth,
                    tileY * TileService.tileHeight,
                    TileService.tileWidth,
                    TileService.tileHeight)
  }
}

object TileId {

  private val tileIdCacheSizeX = 50
  private val tileIdCacheSizeY = 50
  private val tileIdCache: Array[TileId] = new Array[TileId](tileIdCacheSizeX * tileIdCacheSizeY);

  def forLocation(canvasX: Int, canvasY: Int): TileId = {
    // Integer division is truncated towards zero, so if the canvas coordinates are less
    // than zero we subtract one to ensure the negative tile indexes start with -1.
    val tileX = canvasX / TileService.tileWidth - (if (canvasX < 0) 1 else 0)
    val tileY = canvasY / TileService.tileHeight  - (if (canvasY < 0) 1 else 0)

    // Use cached tile id if available, otherwise create new.
    if (tileX >= 0 && tileX < tileIdCacheSizeX &&
        tileY >= 0 && tileY < tileIdCacheSizeY) {

      val index: Int = tileX + tileY * tileIdCacheSizeX

      var cachedId = tileIdCache(index)
      if (cachedId == null) {
        cachedId = new TileId(tileX, tileY)
        tileIdCache(index) = cachedId
      }

      cachedId
    }
    else {
      // Outside cached area, create new
      new TileId(tileX, tileY)
    }
  }

}
