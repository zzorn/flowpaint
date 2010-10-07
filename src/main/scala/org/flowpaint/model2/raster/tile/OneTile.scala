package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.Tile

/**
 * 
 */
object OneTile extends Tile {
  def getByte(index: Int) = 255
  def getFloat(index: Int) = 1

  def setByte(index: Int, value: Byte) = throw UnsupportedOperationException("Not available")
  def setFloat(index: Int, value: Float) = throw UnsupportedOperationException("Not available")
}