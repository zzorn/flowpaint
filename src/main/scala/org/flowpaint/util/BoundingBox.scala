package org.flowpaint.util
/**
 * 
 *
 * @author Hans Haggstrom
 */

class BoundingBox {

  private var empty = true
  private var minX : Int = 0
  private var minY : Int = 0
  private var maxX : Int = 0
  private var maxY : Int = 0

  def isEmpty = empty
  def getMinX = minX
  def getMinY = minY
  def getMaxX = maxX
  def getMaxY = maxY

  def getWidth = if (empty) 0 else maxX - minX + 1
  def getHeight = if (empty) 0 else maxY - minY + 1 

  def clear() {
    empty = true
    minX = 0
    minY = 0
    maxX = 0
    maxY = 0
  }

  def includePoint( x : Int, y : Int ) {
    if (empty) {
      minX = x
      minY = y
      maxX = x
      maxY = y
      empty = false
    }
    else {
      minX = Math.min( minX, x )
      minY = Math.min( minY, y )
      maxX = Math.max( maxX, x )
      maxY = Math.max( maxY, y )
    }
  }

  /**
   * Extends this BoundingBox to include the specified area.
   * The order of x1,y1 and x2,y2 does not matter.
   */
  def includeArea( x1 : Int, y1 : Int, x2 : Int, y2 : Int ) {
    if (empty) {
      minX = Math.min( x1, x2 )
      minY = Math.min( y1, y2 )
      maxX = Math.max( x1, x2 )
      maxY = Math.max( y1, y2 )
      empty = false
    }
    else {
      minX = Math.min( Math.min( minX, x1 ), x2 )
      minY = Math.min( Math.min( minY, y1 ), y2 )
      maxX = Math.max( Math.max( maxX, x1 ), x2 )
      maxY = Math.max( Math.max( maxY, y1 ), y2 )
    }
  }


  def overlaps( x : Int, y : Int ) {
    if (empty) return false else minX <= x && x <= maxX && minY <= y && y <= maxY
  }

}

