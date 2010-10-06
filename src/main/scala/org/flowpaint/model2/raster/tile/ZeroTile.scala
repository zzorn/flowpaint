package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.Tile

/**
 * 
 */
object ZeroTile extends Tile{
  def getByte(index: Int) = 0
  def getFloat(index: Int) = 0f
}