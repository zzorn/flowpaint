package org.flowpaint.model2.raster

/**
 * 
 */
final class ByteTile(val width: Int, val height: Int) extends Tile {

  val data: Array[Byte] = new Array[Byte](width * height)

  private val scaleFactor: Float = 1f / 255f

  def getByte(index: Int) = data(index)
  def getFloat(index: Int) = data(index) * scaleFactor

}