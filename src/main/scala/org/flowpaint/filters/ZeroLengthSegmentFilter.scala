package org.flowpaint.filters

import util.DataSample

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

  val FILTER_DISTANCE: Int = 0

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) = {

    val newX = pointData.getProperty("x",0)
    val newY = pointData.getProperty("y",0)

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