package org.flowpaint.model2.raster

/**
 * 
 */
trait Tile {

  def apply(index: Int): Float = getFloat(index)

  def getFloat(index: Int): Float
  def getByte(index: Int): Int

}