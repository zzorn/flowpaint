package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.TileService

/**
 * 
 */
// TODO: Use own memory management for these with weak references or such, as long as the size is constant
final class DataTile() extends Tile {

  val data: Array[Float] = new Array[Float](width * height)

  def update(index: Int, value: Float) = data(index) = value
  def apply(index: Int) = data(index)

  def copy(): DataTile =  TileService.allocateDataTile(this)

  def copyDataFrom(source: DataTile) {
    Array.copy(source.data, 0, data, 0, source.data.length)
  }

}