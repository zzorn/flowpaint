package org.flowpaint.filters

import property.Data
import util.DataSample
import util.PropertyRegister

/**
 *  Calculates the edges of a stroke based on its radius and angle.
 *
 * @author Hans Haggstrom
 */

class StrokeEdgeCalculatorFilter extends PathProcessor {

    private var previousX = 0f
    private var previousY = 0f
    private var previousLeftX = 0f
    private var previousLeftY = 0f
    private var previousRightX = 0f
    private var previousRightY = 0f


    override protected def onInit() {
        previousX = 0f
        previousY = 0f
        previousLeftX = 0f
        previousLeftY = 0f
        previousRightX = 0f
        previousRightY = 0f
    }

    def processPathPoint(pointData: Data, resultCallback: (Data) => Unit) = {

        // Determine angle and radius
        val DEFAULT_RADIUS = 10f
        val angle = pointData.getFloatProperty(PropertyRegister.ANGLE, 0)
        val radius = pointData.getFloatProperty(PropertyRegister.RADIUS, DEFAULT_RADIUS)

        // Calculate corner points
        val x = pointData.getFloatProperty(PropertyRegister.X, 0)
        val y = pointData.getFloatProperty(PropertyRegister.Y, 0)
        val deltaX = Math.cos(angle).toFloat * radius
        val deltaY = Math.sin(angle).toFloat * radius
        var leftX = x - deltaX
        var leftY = y - deltaY
        var rightX = x + deltaX
        var rightY = y + deltaY

        // Check if either edge crosses the previous one.  In that case, use the previous endpoint
        // Do not apply for the first point
        if (!firstSample && util.GeometryUtils.isLineIntersectingLine(x, y, leftX, leftY, previousX, previousY, previousLeftX, previousLeftY))
            {
                leftX = previousLeftX
                leftY = previousLeftY
            }
        if (!firstSample && util.GeometryUtils.isLineIntersectingLine(x, y, rightX, rightY, previousX, previousY, previousRightX, previousRightY))
            {
                rightX = previousRightX
                rightY = previousRightY
            }

        // Store corner points
        pointData.setFloatProperty(PropertyRegister.LEFT_EDGE_X, leftX)
        pointData.setFloatProperty(PropertyRegister.LEFT_EDGE_Y, leftY)
        pointData.setFloatProperty(PropertyRegister.RIGHT_EDGE_X, rightX)
        pointData.setFloatProperty(PropertyRegister.RIGHT_EDGE_Y, rightY)

        previousX = x
        previousY = y
        previousLeftX = leftX
        previousLeftY = leftY
        previousRightX = rightX
        previousRightY = rightY

        // Continue processing the stroke point
        resultCallback(pointData)
    }

}