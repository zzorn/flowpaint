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

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) {

    val newX = pointData.getProperty("x", 0)
    val newY = pointData.getProperty("y", 0)

    val xDiff = previousX - newX
    val yDiff = previousY - newY

    val angle = HALF_Pi + Math.atan2(yDiff , xDiff).toFloat

    previousX = MathUtils.interpolate(tilt, newX, previousX )
    previousY = MathUtils.interpolate(tilt, newY, previousY )

    pointData.setProperty("angle", angle )

    resultCallback(pointData)
  }


}