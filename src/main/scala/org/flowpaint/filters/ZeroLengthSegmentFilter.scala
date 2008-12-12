package org.flowpaint.filters

import property.Data
import util.DataSample
import util.PropertyRegister

/**
 *  Removes segments with zero length, and merges their properties with subsequent points
 *
 * @author Hans Haggstrom
 */
class ZeroLengthSegmentFilter extends PathProcessor {
    private var previousData = new Data()
    private var temp = new Data()

    private var oldX = 0f
    private var oldY = 0f
    private var oldX2 = 0f
    private var oldY2 = 0f

    override protected def onInit() = {

        previousData.clear
        temp.clear

        oldX = 0f
        oldY = 0f
        oldX2 = 0f
        oldY2 = 0f
    }

    def processPathPoint(pointData: Data, resultCallback: (Data) => Unit) {
        val smoothing = settings.getFloatProperty("smoothing", 0.2f)
        val FILTER_DISTANCE = settings.getFloatProperty("filterDistance", 0f)

        val smooth = if (firstPoint) 0f else smoothing

        val newX = util.MathUtils.interpolate(smooth, pointData.getProperty(PropertyRegister.X, oldX2), oldX2)
        val newY = util.MathUtils.interpolate(smooth, pointData.getProperty(PropertyRegister.Y, oldY2), oldY2)
        /*
            val newX = pointData.getProperty("x",0)
            val newY = pointData.getProperty("y",0)
        */

        pointData.setFloatProperty(PropertyRegister.X, newX)
        pointData.setFloatProperty(PropertyRegister.Y, newY)

        oldX2 = newX
        oldY2 = newY

        if (util.MathUtils.squaredDistance(oldX, oldY, newX, newY) <= FILTER_DISTANCE * FILTER_DISTANCE)
            {
                previousData.setValuesFrom(pointData)

                // Discard (do not process) the point
            }
        else {

            oldX = newX
            oldY = newY

            // Overwrite values with latest ones
            previousData.setValuesFrom(pointData)

            /*
                  temp.clear
                  temp.setValuesFrom(pointData)
            */

            // Copy all values to the newest point, to also catch any old ones that were set for discarded points
            // and not reset with the latest point data.
            pointData.setValuesFrom(previousData)

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