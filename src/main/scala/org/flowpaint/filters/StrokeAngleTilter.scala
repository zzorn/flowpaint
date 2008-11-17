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
  val SMOOTHING = 0.3f

  val HALF_Pi = (0.5 * Math.Pi).toFloat

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) {

    val oldX = previousData.getProperty("x", 0)
    val oldY = previousData.getProperty("y", 0)
    val newX = pointData.getProperty("x", 0)
    val newY = pointData.getProperty("y", 0)

    // Do not change if traveled distance is too low
//    val angle = MathUtils.normalizeAngle( (0.5*Math.Pi+ Math.atan2(newY - previousY, newX - previousX)).toFloat)
//    val angle = (0.5 * Math.Pi + Math.atan2(newY - previousY, newX - previousX)).toFloat


/*
    val angle = {
      val xDiff = oldX - newX
      val yDiff = oldY - newY
      if ( xDiff == 0 ) HALF_Pi
      else if ( yDiff == 0 ) HALF_Pi
      else HALF_Pi + Math.atan2(yDiff , xDiff).toFloat
    }
*/

    val xDiff = previousX - newX
    val yDiff = previousY - newY

    val angle = HALF_Pi + Math.atan2(yDiff , xDiff).toFloat


    //val angle = MathUtils.normalizeAngle((Math.Pi * 0.5f - Math.atan2(newY - previousY, newX - previousX)).toFloat)
//    val angle = 0.5f

/*
    val smoothAngle = MathUtils.wrappedInterpolate(SMOOTHING, angle, previousAngle)
    previousAngle = smoothAngle
*/

    previousX = MathUtils.interpolate(SMOOTHING, newX, previousX )
    previousY = MathUtils.interpolate(SMOOTHING, newY, previousY )

    pointData.setProperty("angle", angle )

    previousData.setValuesFrom(pointData)
    

    resultCallback(pointData)
  }


}