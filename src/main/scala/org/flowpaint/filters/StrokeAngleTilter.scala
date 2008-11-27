package org.flowpaint.brush

import filters.StrokeFilter
import util.DataSample
import util.MathUtils
import Math.abs

/**
 *   Tilts the angle of a stroke to be perpendicular to its direction
 *
 * @author Hans Haggstrom
 */
class StrokeAngleTilter( tilt:Float ) extends StrokeFilter {

  private var previousX =0f
  private var previousY =0f

  private val HALF_Pi = (0.5 * Math.Pi).toFloat

  private var previousAngle = 0f
  private val smooth = 0.8f

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) {

    val newX = pointData.getProperty("x", previousX)
    val newY = pointData.getProperty("y", previousY)

    val xDiff = previousX - newX
    val yDiff = previousY - newY

    val angle = HALF_Pi + Math.atan2(yDiff , xDiff).toFloat

    val normalizedAngle = util.MathUtils.normalizeAngle( angle )
    val smoothedAngle = util.MathUtils.wrappedInterpolate( smooth, normalizedAngle, previousAngle )
    previousAngle = smoothedAngle

/*
    previousX = MathUtils.interpolate(tilt, newX, previousX )
    previousY = MathUtils.interpolate(tilt, newY, previousY )
*/
    previousX = newX
    previousY = newY

/*
    pointData.setProperty("angle", angle )
*/
    pointData.setProperty("angle", smoothedAngle * 2f * Math.Pi.toFloat )

    resultCallback(pointData)
  }


}