package org.flowpaint.model2.blend

import org.flowpaint.model2.raster.{TileService, Tile, DataTile}

/**
 * Copies data from the source using the alpha tile as a mask.
 */
object AlphaBlender extends Blender {
  def blendData(target: DataTile, over: Tile, alpha: Tile) {

    var i = 0
    while (i < TileService.tilePixels) {
      val a = alpha(i)
      target.data(i) = target.data(i) * (1f - a) + over(i) * a
      i += 1
    }
  }
}