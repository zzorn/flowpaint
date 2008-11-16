package org.flowpaint.brush

import filters.StrokeFilter
import util.DataSample
import util.MathUtils

/**
 *   Tilts the angle of a stroke to be perpendicular to its direction
 *
 * @author Hans Haggstrom
 */
class StrokeAngleTilter() extends StrokeFilter {
  val previousData = new DataSample()

  var previousAngle = 0f
  var previousX =0f
  var previousY =0f

  val MINIMUM_MOVEMENT_DISTANCE_FOR_ANGLE_UPDATE = 1000f
  val SMOOTHING = 0.75f

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) {

    val oldX = previousData.getProperty("x", 0)
    val oldY = previousData.getProperty("y", 0)
    val newX = pointData.getProperty("x", 0)
    val newY = pointData.getProperty("y", 0)

    // Do not change if traveled distance is too low
    val angle = if (MathUtils.squaredDistance(previousX, previousY, newX, newY) > MINIMUM_MOVEMENT_DISTANCE_FOR_ANGLE_UPDATE )
      {
        previousX = newX
        previousY = newY
        MathUtils.normalizeAngle((Math.Pi * 0.5f + Math.atan2(newY - oldY, newX - oldX)).toFloat)
      }
      else
        previousAngle

    val smoothAngle = MathUtils.wrappedInterpolate(SMOOTHING, angle, previousAngle)
    previousAngle = smoothAngle

    pointData.setProperty("angle", smoothAngle)

    previousData.setValuesFrom(pointData)

    resultCallback(pointData)
  }


}