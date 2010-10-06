package org.flowpaint.model2.raster.tile

import org.flowpaint.model2.raster.Tile

/**
 * 
 */
object OneTile extends Tile {
  def getByte(index: Int) = 255
  def getFloat(index: Int) = 1
}