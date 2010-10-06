package org.flowpaint.model2

import _root_.org.flowpaint.util.Rectangle
import collection.mutable.HashMap
import collection._

/**
 * 
 */
// TODO: Undo history saving - but do it with the command history - they can store the blocks of the layers that they changed
class Raster {

  private val blockSize = 64

  // The channels present in this raster
  private var channels: Set[Symbol] = Nil
  
  // Map from row indexes to map from column indexes to blocks of pixel data.
  private val blocks: mutable.Map[Int, Map[Int, Block]] = new HashMap()

  def getBlocks(area: Rectangle): List[Block] = {
    val xc1 = area.x1 / blockSize
    val yc1 = area.y1 / blockSize
    val xc2 = area.x2 / blockSize
    val yc2 = area.y2 / blockSize

    var result: List[Block] = Nil

    var yc = yc1
    while(yc <= yc2) {
      val columns = blocks.get(yc)

      if (columns != null) {
        var xc = xc1
        while(xc <= xc2) {
          val block = columns.get(xc)
          if (block != null) result = block :: result

          xc += 1
        }
      }

      yc += 1
    }

    result
  }

  /**
   * Renders the specified raster on top of this raster, for the specified area.
   */
  def overlay(raster: Raster, area: Rectangle) {
    // TODO
  }

}

