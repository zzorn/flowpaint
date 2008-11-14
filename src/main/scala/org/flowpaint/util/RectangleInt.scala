package org.flowpaint.util
/**
 * A rectangluar area defined with integer coordinates.
 *
 * @author Hans Haggstrom
 */

case class RectangleInt (x1: Int, y1: Int, x2: Int, y2: Int) {

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