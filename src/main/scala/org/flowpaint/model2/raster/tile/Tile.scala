package org.flowpaint.model2.raster

/**
 * 
 */
trait Tile {

  final def width = TileService.tileWidth
  final def height = TileService.tileHeight

  def apply(index: Int): Float
  def update(index: Int, value: Float)

  final def getByte(index: Int): Int = (255 * apply()).toInt

  /**
   * A new tile which is a copy of this tile.
   * Any changed to this tile will not affect the created copy.
   */
  def copy(): Tile
  

}