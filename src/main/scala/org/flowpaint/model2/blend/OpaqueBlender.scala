package org.flowpaint.model2.blend

import org.flowpaint.model2.raster.{TileService}
import org.flowpaint.model2.raster.tile.{Tile, DataTile}

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