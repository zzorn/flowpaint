package org.flowpaint.model2.blend

import org.flowpaint.model2.raster.{DataTile, Tile}

/**
 * Trait for functions that blend a layer with an underlying one.
 */
trait Blender {
  def blendBackground(under: Tile, over: Tile, alpha: Tile): Tile

  def blendData(target: DataTile, over: Tile, alpha: Tile)
}
