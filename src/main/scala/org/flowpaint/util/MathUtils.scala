package org.flowpaint.util

/**
 *
 *
 * @author Hans Haggstrom
 */

object MathUtils {
  def interpolate(t: Float, a: Float, b: Float): Float = (1.0f - t) * a + t * b

  def squaredDistance(x1:Float, y1:Float, x2:Float, y2:Float) : Float = {
    val xDiff = x2-x1
    val yDiff = y2 - y1
    xDiff*xDiff+yDiff*yDiff
  }


  /**
   *  Turn an angle in radians to a normalized value between 0 to 1.
   */
  def normalizeAngle(angleRadians: Float): Float = wrapToZeroToOne(angleRadians / (Math.Pi.toFloat * 2f))

  
  def wrapToZeroToOne(a: Float): Float = {
    val v = a % 1f

    if (v < 0)
      v + 1
    else
      v
  }

  /**
   *  The distance between two numbers between 0 and 1, if we assume that the value can roll over between 0 and 1
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
      wrapToZeroToOne( interpolate(t, an, bn) )
    else if (an < bn) // Wrap a up, then roll back when interpolation done
      wrapToZeroToOne(interpolate(t, an + 1, bn))
    else // Wrap b up, then roll back when interpolation done
      wrapToZeroToOne(interpolate(t, an, bn + 1))
  }


}