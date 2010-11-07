package org.flowpaint.util
/**
 * A rectangluar area defined with integer coordinates.
 *
 * @author Hans Haggstrom
 */

trait Rectangle {

  def x1 : Int
  def y1 : Int
  def width : Int
  def height : Int

  def x2 : Int = x1 + width
  def y2 : Int = y1 + height

  def centerX = x1 + width / 2
  def centerY = y1 + height / 2

  def contains( x : Int, y : Int ) = x >= x1 && x < x2 && y >= y1 && y < y2

  def intersects(x: Int, y: Int, w: Int, h: Int): Boolean = {
    return x1 < x + w && x < x2 &&
           y1 < y + h && y < y2
  }

  def union( r2 : Rectangle ) : Rectangle = {
    val nx1 = x1 min r2.x1
    val ny1 = y1 min r2.y1
    val nw = (x2 max r2.x2) - nx1
    val nh = (y2 max r2.y2) - ny1
    RectangleImpl( nx1, ny1, nw, nh )
  }

  def iterate( minX : Float, minY : Float,maxX : Float,maxY : Float, visitor: (Int, Int) => Unit) {

    // Use segment bounding box to reudce the area needed to be iterated through
    val sX = Math.max ( minX.toInt, x1 )
    val sY = Math.max ( minY.toInt, y1 )
    val eX = Math.min ( maxX.toInt, x2 )
    val eY = Math.min ( maxY.toInt, y2 )

    for (y <- sY to eY;
         x <- sX to eX) visitor(x, y)

  }
}