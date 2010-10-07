package org.flowpaint.model2.raster

/**
 * 
 */
// TODO: Use own memory management for these with weak references or such, as long as the size is constant
final class ByteTile(val width: Int, val height: Int) extends Tile {

  val data: Array[Byte] = new Array[Byte](width * height)

  private val scaleFactor: Float = 1f / 255f
  private val invScaleFactor: Float = 255f

  def getByte(index: Int) = data(index)
  def getFloat(index: Int) = data(index) * scaleFactor
  def setByte(index: Int, value: Byte) = data(index) = value 
  def setFloat(index: Int, value: Float) = data(index) = value * invScaleFactor

}