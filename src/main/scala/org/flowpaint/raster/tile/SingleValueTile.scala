package org.flowpaint.raster.tile


/**
 * A tile with just one value for all positions.
 */
trait SingleValueTile extends Tile {
  def value: Float
}