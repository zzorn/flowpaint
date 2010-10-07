package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.Tile

/**
 * 
 */
object OneTile extends SingleValueTile {
  def value = 0f
  def apply(index: Int) = 1f
  def update(index: Int, value: Float) = throw UnsupportedOperationException("Not supported")
  def copy() = this

}