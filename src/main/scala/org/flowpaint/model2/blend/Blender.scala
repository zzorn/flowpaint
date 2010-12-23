package org.flowpaint.model2.blend

import org.flowpaint.model2.raster.tile.{DataTile, Tile}
import org.flowpaint.model2.raster.TileService

/**
 * Trait for functions that blend a layer with an underlying one.
 */
trait Blender {

  def blendBackground(under: Tile, over: Tile, alpha: Tile): Tile = {
    val result = TileService.allocateDataTile(under)
    blendData(result, over, alpha)
    result
  }

  def blendData(target: DataTile, over: Tile, alpha: Tile)
}
