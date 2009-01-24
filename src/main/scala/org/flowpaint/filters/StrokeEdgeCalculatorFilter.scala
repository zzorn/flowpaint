package org.flowpaint.filters

import property.Data
import util.{DataSample, PropertyRegister, MathUtils}
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

    private var rightEdgeScale = 1f
    private var leftEdgeScale = 1f

    private var previousAngle = 0f

    private val angleScale = 2f * Math.Pi.toFloat

    override protected def onInit() {
        previousX = 0f
        previousY = 0f
        previousLeftX = 0f
        previousLeftY = 0f
        previousRightX = 0f
        previousRightY = 0f

        rightEdgeScale = 1f
        leftEdgeScale = 1f

        previousAngle = 0f
    }


    protected def processPathPoint(pointData: Data) : List[Data] =  {

        // Determine angle and radius
        val DEFAULT_RADIUS = 10f
        val angle = pointData.getFloatProperty(PropertyRegister.ANGLE, 0f)
        val radius = pointData.getFloatProperty(PropertyRegister.RADIUS, DEFAULT_RADIUS)

        // Calculate corner points
        val x = pointData.getFloatProperty(PropertyRegister.PATH_X, 0)
        val y = pointData.getFloatProperty(PropertyRegister.PATH_Y, 0)

        var leftX = x - Math.cos(angle).toFloat * radius * leftEdgeScale
        var leftY = y - Math.sin(angle).toFloat * radius * leftEdgeScale

        var rightX = x + Math.cos(angle).toFloat * radius * rightEdgeScale
        var rightY = y + Math.sin(angle).toFloat * radius * rightEdgeScale

        val recoverySpeed = getFloatProperty( "cornerScaleRecoverySpeed", 0.03f )

        leftEdgeScale = Math.min( 1f, leftEdgeScale + recoverySpeed )
        rightEdgeScale = Math.min( 1f, rightEdgeScale + recoverySpeed )

        val turningLeft = MathUtils.wrappedClosestDelta( previousAngle / angleScale, angle / angleScale ) > 0f

        // Check if either edge crosses the previous one.  In that case, use the previous endpoint
        // Do not apply for the first point
        if (!isFirstPoint) {
            if ( /*turningLeft &&*/ util.GeometryUtils.isLineIntersectingLine(x, y, leftX, leftY, previousX, previousY, previousLeftX, previousLeftY))
                {
                    leftX = previousLeftX
                    leftY = previousLeftY
                    leftEdgeScale = if (radius == 0) 0 else MathUtils.distance( x, y, leftX, leftY ) / radius
                }
            if ( /*!turningLeft &&*/ util.GeometryUtils.isLineIntersectingLine(x, y, rightX, rightY, previousX, previousY, previousRightX, previousRightY))
                {
                    rightX = previousRightX
                    rightY = previousRightY
                    rightEdgeScale = if (radius == 0) 0 else MathUtils.distance( x, y, rightX, rightY ) / radius
                }
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
        previousAngle = angle

        // Continue processing the stroke point
        List(pointData)
    }

}