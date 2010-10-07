package org.flowpaint.model2.raster

/**
 * 
 */
trait Tile {

  def apply(index: Int): Float = getFloat(index)
  def update(index: Int, value: Float) = setFloat(index, value)

  def getFloat(index: Int): Float
  def setFloat(index: Int, value: Float)
  def getByte(index: Int): Int
  def setByte(index: Int, value: Byte)

}