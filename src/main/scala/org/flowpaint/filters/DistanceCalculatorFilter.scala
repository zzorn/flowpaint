package org.flowpaint.filters
import util.{DataSample, PropertyRegister, MathUtils}

/**
 * Calculates the distance along the line, as well as the length of the previous stroke.
 * 
 * @author Hans Haggstrom
 */
// TODO: Add velocity calculation too
class DistanceCalculatorFilter extends StrokeFilter {

  var previousX = 0f
  var previousY = 0f
  var previousDistance = 0f

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit)  {

    val firstPoint = pointData.getProperty(PropertyRegister.INDEX, 2 ) < 0.5f
    val x = pointData.getProperty(PropertyRegister.X, 0 )
    val y = pointData.getProperty(PropertyRegister.Y, 0 )

    if (firstPoint) previousDistance = 0

    val previousSegmentLength = if (firstPoint) 0f else MathUtils.distance( previousX, previousY, x, y )

    pointData.setProperty( PropertyRegister.PREVIOUS_SEGMENT_LENGTH, previousSegmentLength )
    pointData.setProperty( PropertyRegister.DISTANCE, previousDistance + previousSegmentLength )

    previousX = x
    previousY = y
    previousDistance += previousSegmentLength

    resultCallback( pointData )
  }
}