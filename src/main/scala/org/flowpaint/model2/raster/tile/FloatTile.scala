package org.flowpaint.model2.raster

/**
 * 
 */
// TODO: Use own memory management for these with weak references or such, as long as the size is constant
final class FloatTile(val width: Int, val height: Int) extends Tile {

  val data: Array[Float] = new Array[Float](width * height)

  private val scaleFactor: Float = 255f

  def getByte(index: Int) = (data(index) * scaleFactor).toInt
  def getFloat(index: Int) = data(index)

}