package org.flowpaint.util

/**
 *  Geometry related utility functions.
 *
 * @author Hans Haggstrom
 */

object GeometryUtils {

  /**
   * @return true if c is between a and b, or equal to a or b.
   */
  def isBetween(a: Float, b: Float, c: Float): Boolean =
    {
      if (b > a) c >= a && c <= b else c >= b && c <= a
    }


  /**
   *  Check if two line segments intersects.
   *
   * @param ax0 First line start x.
   * @param ay0 First line start y.
   * @param ax1 First line end x.
   * @param ay1 First line end y.
   * @param bx0 Second line start x.
   * @param by0 Second line start y.
   * @param bx1 Second line end x.
   * @param by1 Second line end y.
   *
   * @return True if the two lines intersects.
   */
  def isLineIntersectingLine(ax0: Float, ay0: Float, ax1: Float, ay1: Float,
                            bx0: Float, by0: Float, bx1: Float, by1: Float): Boolean =
    {
      val s1 = sameSide(ax0, ay0, ax1, ay1, bx0, by0, bx1, by1)
      val s2 = sameSide(bx0, by0, bx1, by1, ax0, ay0, ax1, ay1)

      return s1 <= 0 && s2 <= 0
    }


  /**
   *  Check if two points are on the same side of a given line.
   *  Algorithm from Sedgewick page 350.
   *
   * @param x0 Line start x.
   * @param y0 Line start y.
   * @param x1 Line end x.
   * @param y1 Line end y.
   * @param px0 First point x.
   * @param py0 First point y.
   * @param px1 Second point x.
   * @param py1 Second point y.
   *
   * @return < 0 if points on opposite sides, <br>
   *          = 0 if one of the points is exactly on the line, or <br>
   *          > 0 if points on same side. <br>
   */
  def sameSide(x0: Float, y0: Float, x1: Float, y1: Float,
              px0: Float, py0: Float, px1: Float, py1: Float): Int =
    {
      val sameSide = 0

      val dx = x1 - x0
      val dy = y1 - y0
      val dx1 = px0 - x0
      val dy1 = py0 - y0
      val dx2 = px1 - x1
      val dy2 = py1 - y1

      // Cross product of the vector from the endpoint of the line to the point
      val c1 = dx * dy1 - dy * dx1;
      val c2 = dx * dy2 - dy * dx2;

      if (c1 != 0 && c2 != 0)
        {
          sameSide = if (c1 < 0 == c2 < 0) 1 else -1
        }
      else if (dx == 0 && dx1 == 0 && dx2 == 0)
        {
          sameSide = if (!isBetween(y0, y1, py0) && !isBetween(y0, y1, py1)) 1 else 0
        }
      else if (dy == 0 && dy1 == 0 && dy2 == 0)
        {
          sameSide = if (!isBetween(x0, x1, px0) && !isBetween(x0, x1, px1)) 1 else 0
        }

      return sameSide;
    }


}