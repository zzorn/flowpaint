package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.Tile

/**
 * 
 */
object ZeroTile extends Tile{
  def getByte(index: Int) = 0
  def getFloat(index: Int) = 0f

  def setByte(index: Int, value: Byte) = throw UnsupportedOperationException("Not available")
  def setFloat(index: Int, value: Float) = throw UnsupportedOperationException("Not available")

}