package org.flowpaint.brush

import filters.StrokeFilter
import util.DataSample
import util.MathUtils
import Math.abs

import util.PropertyRegister

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

  private var firstPoint :DataSample = null

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) {

    val first = pointData.getProperty(PropertyRegister.INDEX, 2) < 1

    val newX = pointData.getProperty(PropertyRegister.X, previousX)
    val newY = pointData.getProperty(PropertyRegister.Y, previousY)

    val xDiff = previousX - newX
    val yDiff = previousY - newY

    val angle = HALF_Pi + Math.atan2(yDiff , xDiff).toFloat

    val normalizedAngle = util.MathUtils.normalizeAngle( angle )

    val smoothing = if (first || firstPoint != null) 0f else smooth
    val smoothedAngle = util.MathUtils.wrappedInterpolate( smoothing, normalizedAngle, previousAngle )
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
    pointData.setProperty(PropertyRegister.ANGLE, smoothedAngle * 2f * Math.Pi.toFloat )

    if ( first ){
      // Only store the first point, don't send it forward yet
      firstPoint = new DataSample()
      firstPoint.setValuesFrom( pointData )
    }
    else {
      if (firstPoint != null){
        // Forward the stored first point, using the current angle
        firstPoint.setProperty(PropertyRegister.ANGLE, smoothedAngle * 2f * Math.Pi.toFloat )
        resultCallback(firstPoint)
        firstPoint = null
      }

      resultCallback(pointData)
    }

  }


}