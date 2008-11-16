package org.flowpaint.util
/**
 * 
 *
 * @author Hans Haggstrom
 */

object MathUtils {

  def interpolate(t: Float, a: Float, b: Float): Float = (1.0f - t) * a + t * b



}