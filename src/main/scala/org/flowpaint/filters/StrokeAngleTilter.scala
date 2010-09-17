package org.flowpaint.filters

import org.flowpaint.property.{DataImpl, Data}
import Math.abs

import org.flowpaint.util.PropertyRegister
import org.flowpaint.util.{PropertyRegister, DataSample, MathUtils}

/**
 *    Tilts the angle of a stroke to be perpendicular to its direction
 *
 * @author Hans Haggstrom
 */
class StrokeAngleTilter() extends PathProcessor {
    private val HALF_Pi = (0.5 * Math.Pi).toFloat

    private var previousX = 0f
    private var previousY = 0f
    private var previousAngle = 0f

    private var firstPointData: Data = null

    override protected def onInit() {

        previousX = 0f
        previousY = 0f
        previousAngle = 0f

        firstPointData = null
    }


    protected def processPathPoint(pointData: Data) : List[Data] =  {

        var result : List[Data] = Nil

        val smooth = getFloatProperty("smooth", pointData, 0f)
        val tilt = getFloatProperty("tilt", pointData, 0f)

        val newX = pointData.getFloatProperty(PropertyRegister.PATH_X, previousX)
        val newY = pointData.getFloatProperty(PropertyRegister.PATH_Y, previousY)

        val xDiff = previousX - newX
        val yDiff = previousY - newY

        val angle = HALF_Pi + Math.atan2(yDiff, xDiff).toFloat

        val normalizedAngle = MathUtils.normalizeAngle(angle)

        val smoothing = if (firstPoint || firstPointData != null) 0f else smooth
        val smoothedAngle = MathUtils.wrappedInterpolate(smoothing, normalizedAngle, previousAngle)
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

        pointData.setFloatProperty(PropertyRegister.ANGLE, smoothedAngle * 2f * Math.Pi.toFloat)


        if (firstPoint) {
            // Only store the first point, don't send it forward yet
            firstPointData = new DataImpl()
            firstPointData.setValuesFrom(pointData)
        }
        else {
            if (firstPointData != null) {
                // Forward the stored first point, using the current angle
                firstPointData.setFloatProperty(PropertyRegister.ANGLE, smoothedAngle * 2f * Math.Pi.toFloat)
                result = List(firstPointData)
                firstPointData = null
            }

            result = result ::: List(pointData)
        }

      return result
    }



}