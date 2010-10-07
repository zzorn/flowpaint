package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.Tile

/**
 * 
 */
trait SingleValueTile extends Tile {
  def value: Float
}