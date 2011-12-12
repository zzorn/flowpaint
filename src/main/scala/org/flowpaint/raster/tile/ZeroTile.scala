package org.flowpaint.raster.tile


/**
 * A tile with value zero on all positions
 */
object ZeroTile extends SingleValueTile {
  def value = 0f
  def apply(index: Int) = 0f
  def update(index: Int, value: Float) {throw new UnsupportedOperationException("Not supported")}

  def copy() = this
}