package org.flowpaint.model2.raster.tile


/**
 * 
 */
object OneTile extends SingleValueTile {
  def value = 0f
  def apply(index: Int) = 1f
  def update(index: Int, value: Float) = throw new UnsupportedOperationException("Not supported")
  def copy() = this

}