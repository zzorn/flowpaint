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
class StrokeAngleTilter(  ) extends StrokeFilter {


  var smoothing = 0f

  private val previousData = new DataSample()

  private var previousAngle = 0f
  private var previousX =0f
  private var previousY =0f

  private val MINIMUM_MOVEMENT_DISTANCE_FOR_ANGLE_UPDATE = 1000f


  private val HALF_Pi = (0.5 * Math.Pi).toFloat

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) {

/*
    val oldX = previousData.getProperty("x", 0)
    val oldY = previousData.getProperty("y", 0)
*/
    val newX = pointData.getProperty("x", 0)
    val newY = pointData.getProperty("y", 0)

    val xDiff = previousX - newX
    val yDiff = previousY - newY

    /*
    val angle = if (xDiff == 0 && yDiff == 0) previousAngle
    else {

      previousAngle = HALF_Pi + Math.atan2(yDiff , xDiff).toFloat
      previousAngle
    }
*/

    val angle = HALF_Pi + Math.atan2(yDiff , xDiff).toFloat

    previousX = MathUtils.interpolate(smoothing, newX, previousX )
    previousY = MathUtils.interpolate(smoothing, newY, previousY )

    pointData.setProperty("angle", angle )


/*
    previousData.setValuesFrom(pointData)
*/

    resultCallback(pointData)
  }


}