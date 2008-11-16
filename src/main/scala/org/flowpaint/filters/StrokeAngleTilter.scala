package org.flowpaint.brush

import filters.StrokeFilter
import util.DataSample
import util.MathUtils

/**
 * Tilts the angle of a stroke to be perpendicular to its direction
 *
 * @author Hans Haggstrom
 */
class StrokeAngleTilter( ) extends StrokeFilter {

  val previousData = new DataSample()

  var previousAngle = 0f

  val SMOOTHING = 0.75f

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit)  {

    val oldX = previousData.getProperty("x",0)
    val oldY = previousData.getProperty("y",0)
    val newX = pointData.getProperty("x",0)
    val newY = pointData.getProperty("y",0)

    val angle = MathUtils.normalizeAngle( (Math.Pi * 0.5f + Math.atan2( newY - oldY, newX - oldX )).toFloat )

    val smoothAngle = MathUtils.wrappedInterpolate( SMOOTHING, angle, previousAngle )
    previousAngle = smoothAngle

    pointData.setProperty("angle", smoothAngle)

    previousData.setValuesFrom( pointData )

    resultCallback( pointData )
  }


}