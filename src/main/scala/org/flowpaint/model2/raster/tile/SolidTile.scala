package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.{Tile, TileId}

/**
 * A tile with a fixed color. 
 */
final case class SolidTile(value: Float) extends SingleValueTile {
  def apply(index: Int) = value
  def update(index: Int, value: Float) = throw new UnsupportedOperationException("Not supported")
  def copy() = this

}