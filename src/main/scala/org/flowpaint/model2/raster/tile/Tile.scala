package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.TileService

/**
 * 
 */
trait Tile {

  final def width = TileService.tileWidth
  final def height = TileService.tileHeight

  final def apply(tileX: Int, tileY: Int): Float = apply(tileY * TileService.tileWidth + tileX)
  final def update(tileX: Int, tileY: Int, value: Float): Unit = update(tileY * TileService.tileWidth + tileX, value)
  def apply(index: Int): Float
  def update(index: Int, value: Float): Unit

  final def getByte(index: Int): Int = (255 * apply(index)).toInt

  /**
   * A new tile which is a copy of this tile.
   * Any changed to this tile will not affect the created copy.
   */
  def copy(): Tile
  

}