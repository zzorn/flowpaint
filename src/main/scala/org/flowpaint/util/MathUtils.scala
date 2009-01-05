package org.flowpaint.util

/**
 *
 *
 * @author Hans Haggstrom
 */

object MathUtils {
  def lerp(t: Float, a: Float, b: Float): Float = (1.0f - t) * a + t * b
  def interpolate(t: Float, a: Float, b: Float): Float = lerp( t, a, b )

  def interpolate(input: Float, inputStart: Float, inputEnd: Float, outputStart : Float, outputEnd : Float): Float = {

    // Check for special case where start and end positions are the same.  In this case return the average value.
    if ( inputStart == inputEnd )
    {
        return 0.5f * ( outputStart + outputEnd );
    }

    val relativePosition =  ( input - inputStart ) / ( inputEnd - inputStart );

    outputStart + relativePosition * ( outputEnd - outputStart );
  }


  def squaredDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float = {
    val xDiff = x2 - x1
    val yDiff = y2 - y1
    xDiff * xDiff + yDiff * yDiff
  }

  def distance(x1: Float, y1: Float, x2: Float, y2: Float): Float = {
    val xDiff = x2 - x1
    val yDiff = y2 - y1
    Math.sqrt( xDiff * xDiff + yDiff * yDiff ).toFloat
  }



  def normalizeAngle(angleRadians: Double): Float = normalizeAngle(angleRadians.toFloat)

  /**
   *    Turn an angle in radians to a normalized value between 0 to 1.
   */
  def normalizeAngle(angleRadians: Float): Float = wrapToZeroToOne(angleRadians / (Math.Pi.toFloat * 2f))


  def clampToZeroToOne(a: Float): Float = {
    if (a < 0f)
      0f
    else if (a > 1f)
      1f
    else
      a
  }

  def wrapToZeroToOne(a: Float): Float = {
    val v = a % 1f

    if (v < 0)
      v + 1
    else
      v
  }

  /**
   * Converts a function that maps doubles to -1..1 to a function that maps floats or doubles to 0..1 floats.
   */
  def normalizeFunction(f: (Double) => Double): (Float) => Float =
    (v: Float) => (0.5 + 0.5 * f(v)).toFloat

  /**
   *    The distance between two numbers between 0 and 1, if we assume that the value can roll over between 0 and 1
   */
  def wrappedDistance(a: Float, b: Float): Float = {
    // Roll to 0 to 1 range
    val an = wrapToZeroToOne(a)
    val bn = wrapToZeroToOne(b)

    // Determine order
    var smaller = an
    var larger = bn
    if (an > bn) {
      smaller = bn
      larger = an
    }

    // Calculate direct distance, and rolled over distance
    val directDistance = larger - smaller
    val rolledDistance = smaller + (1 - larger)

    // Return smallest distance
    if (directDistance < rolledDistance)
      directDistance
    else
      rolledDistance
  }


  def wrappedInterpolate(t: Float, a: Float, b: Float): Float = {
    // Roll to 0 to 1 range
    val an = wrapToZeroToOne(a)
    val bn = wrapToZeroToOne(b)

    if (Math.abs(an - bn) <= 0.5) // Closer through direct path than wrapped path
      wrapToZeroToOne(interpolate(t, an, bn))
    else if (an < bn) // Wrap a up, then roll back when interpolation done
      wrapToZeroToOne(interpolate(t, an + 1, bn))
    else // Wrap b up, then roll back when interpolation done
      wrapToZeroToOne(interpolate(t, an, bn + 1))
  }

  /**
   * @return true if c is between a and b.
   */
  def isBetween(a: Float, b: Float, c: Float): Boolean =
    {
      if (b > a)
        c >= a && c <= b
      else
        c >= b && c <= a
    }


  /**
   *   Check if two points are on the same side of a given line.
   *   Algorithm from Sedgewick page 350.
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
   *           = 0 if one of the points is exactly on the line, or <br>
   *           > 0 if points on same side. <br>
   */
  def sameSide(x0: Float, y0: Float, x1: Float, y1: Float,
              px0: Float, py0: Float, px1: Float, py1: Float): Int =
    {
      var sameSide = 0

      val dx = x1 - x0
      val dy = y1 - y0;
      val dx1 = px0 - x0;
      val dy1 = py0 - y0;
      val dx2 = px1 - x1;
      val dy2 = py1 - y1;

      // Cross product of the vector from the endpoint of the line to the point
      val c1 = dx * dy1 - dy * dx1
      val c2 = dx * dy2 - dy * dx2

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


  def rightOf(x0: Float, y0: Float,
             x1: Float, y1: Float,
             px0: Float, py0: Float): Boolean = {

    val xDiff = x1 - x0
    val yDiff = y1 - y0
    val rightX = x0 - yDiff
    val rightY = y0 + xDiff

    sameSide(x0, y0, x1, y1, px0, py0, rightX, rightY) > 0

  }

}
