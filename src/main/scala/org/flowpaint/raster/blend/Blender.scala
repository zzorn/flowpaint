package org.flowpaint.raster.blend

import org.flowpaint.raster.tile.{TileService, DataTile, Tile}
import org.flowpaint.util.StringUtils


/**
 * Trait for functions that blend a layer with an underlying one.
 */
trait Blender {

  def name: String = getClass.getSimpleName

  def blendBackground(under: Tile, over: Tile, alpha: Tile): Tile = {
    val result = TileService.allocateDataTile(under)
    blendData(result, over, alpha)
    result
  }

  def blendData(target: DataTile, over: Tile, alpha: Tile)
}
