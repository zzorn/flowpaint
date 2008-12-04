package org.flowpaint.filters

import util.DataSample
import util.PropertyRegister

/**
 * Removes segments with zero length, and merges their properties with subsequent points
 *
 * @author Hans Haggstrom
 */
class ZeroLengthSegmentFilter extends StrokeFilter {

  private var previousData = new DataSample()
  private var temp = new DataSample()

  private var oldX = 0f
  private var oldY = 0f
  private var oldX2 = 0f
  private var oldY2 = 0f

  val FILTER_DISTANCE: Int = 0

  val smoothing = 0.2f

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) = {


/*
    pointData.setProperty("x",pointData.getProperty("x",oldX2))
    pointData.setProperty("y",pointData.getProperty("y",oldY2))
*/

    val index = pointData.getProperty(PropertyRegister.INDEX, 0f )
    val smooth = if( index == 0f ) 0f else smoothing

    val newX = util.MathUtils.interpolate(smooth, pointData.getProperty(PropertyRegister.X,oldX2), oldX2 )
    val newY = util.MathUtils.interpolate(smooth,  pointData.getProperty(PropertyRegister.Y,oldY2), oldY2 )
/*
    val newX = pointData.getProperty("x",0)
    val newY = pointData.getProperty("y",0)
*/

    pointData.setProperty(PropertyRegister.X,newX)
    pointData.setProperty(PropertyRegister.Y,newY)

    oldX2 = newX
    oldY2 = newY

    if ( util.MathUtils.squaredDistance( oldX, oldY, newX, newY ) <= FILTER_DISTANCE * FILTER_DISTANCE )
      {
        previousData.setValuesFrom( pointData )

        // Discard (do not process) the point
      }
    else {

      oldX = newX
      oldY = newY

      // Overwrite values with latest ones
      previousData.setValuesFrom( pointData )

/*
      temp.clear
      temp.setValuesFrom(pointData)
*/

      // Copy all values to the newest point, to also catch any old ones that were set for discarded points
      // and not reset with the latest point data.
      pointData.setValuesFrom( previousData )

/*
      // Clear the old temp data, but retain the most recent
//      previousData.clear
      previousData.setValuesFrom( temp )
*/

      // Process normally
      resultCallback(pointData)
    }



  }
}