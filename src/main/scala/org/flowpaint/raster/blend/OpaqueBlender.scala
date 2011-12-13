package org.flowpaint.raster.blend

import org.flowpaint.raster.tile.{TileService, Tile, DataTile}


/**
 * Replaces the target completely with the source without using alpha.
 */
object OpaqueBlender extends Blender {

  def blendData(target: DataTile, over: Tile, alpha: Tile) {
    var i = 0
    while (i < TileService.tilePixels) {
      target.data(i) = over(i)
      i += 1
    }
  }

}