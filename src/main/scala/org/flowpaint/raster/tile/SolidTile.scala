package org.flowpaint.raster.tile


/**
 * A tile with a specified single value for all positions.
 */
final case class SolidTile(value: Float) extends SingleValueTile {
  def apply(index: Int) = value
  def update(index: Int, value: Float) {throw new UnsupportedOperationException("Not supported") }
  def copy() = this

}