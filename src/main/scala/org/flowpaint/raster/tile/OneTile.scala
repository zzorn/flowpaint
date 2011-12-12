package org.flowpaint.raster.tile


/**
 * A tile with value of one on all positions.
 */
object OneTile extends SingleValueTile {
  def value = 1f
  def apply(index: Int) = 1f
  def update(index: Int, value: Float) { throw new UnsupportedOperationException("Not supported") }
  def copy() = this

}