package org.flowpaint.filters

import property.{DataImpl, Data}
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

    private def roundCorner(pointData: Data, angle : Float, previousAngle : Float, angleDelta : Float, radius : Float) : List[Data] =  {

        val angleScale = 2f * Math.Pi.toFloat
        val roundingAngle = 1f / 16f

        var result : List[Data] = Nil

        val turningLeft = angleDelta > 0f

        if (!isFirstPoint) {

            if ( angle != previousAngle ) {

                val normPrevAngle = previousAngle / angleScale
                val normNewAngle = angle / angleScale

                val distance = MathUtils.wrappedDistance( normPrevAngle, normNewAngle )

                if (distance > roundingAngle ) {

                    // Add rounding points
                    val steps = (distance / roundingAngle).toInt

                    for ( i <- 0 to steps) {

                        val t : Float = (i.toFloat) / (steps.toFloat)
                        val stepAngle = MathUtils.wrappedInterpolate( t, normPrevAngle, normNewAngle )
                        val invStepAngle = MathUtils.wrappedInterpolateLongerWay( t, normPrevAngle, normNewAngle )

                        val data = new DataImpl( pointData )

                        val cornerAngle = stepAngle * angleScale
                        val invCornerAngle = invStepAngle * angleScale
                        data.setFloatProperty(PropertyRegister.ANGLE, cornerAngle  )

                        // Calculate corner points
                        val x = pointData.getFloatProperty(PropertyRegister.PATH_X, 0)
                        val y = pointData.getFloatProperty(PropertyRegister.PATH_Y, 0)

                        val leftAngle = if (!turningLeft) cornerAngle else invCornerAngle
                        val rightAngle = if (turningLeft) cornerAngle else invCornerAngle

                        var leftX = x - Math.cos(leftAngle).toFloat * radius * leftEdgeScale
                        var leftY = y - Math.sin(leftAngle).toFloat * radius * leftEdgeScale

                        var rightX = x + Math.cos(rightAngle).toFloat * radius * rightEdgeScale
                        var rightY = y + Math.sin(rightAngle).toFloat * radius * rightEdgeScale
/*
                        var leftX = x - (if (turningLeft) 0f else Math.cos(cornerAngle).toFloat * radius * leftEdgeScale)
                        var leftY = y - (if (turningLeft) 0f else Math.sin(cornerAngle).toFloat * radius * leftEdgeScale)

                        var rightX = x + (if (!turningLeft) 0f else Math.cos(cornerAngle).toFloat * radius * rightEdgeScale)
                        var rightY = y + (if (!turningLeft) 0f else Math.sin(cornerAngle).toFloat * radius * rightEdgeScale)
*/

                        // Store corner points
                        data.setFloatProperty(PropertyRegister.PATH_X, x)
                        data.setFloatProperty(PropertyRegister.PATH_Y, y)
                        data.setFloatProperty(PropertyRegister.LEFT_EDGE_X, leftX)
                        data.setFloatProperty(PropertyRegister.LEFT_EDGE_Y, leftY)
                        data.setFloatProperty(PropertyRegister.RIGHT_EDGE_X, rightX)
                        data.setFloatProperty(PropertyRegister.RIGHT_EDGE_Y, rightY)


                        result = result ::: List( data )
                    }

                }
            }
        }

        result
    }



    protected def processPathPoint(pointData: Data) : List[Data] =  {

        var result  : List[Data] = Nil

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

        val angleDelta : Float = MathUtils.wrappedClosestDelta( previousAngle / angleScale, angle / angleScale )

        result = roundCorner( pointData, angle, previousAngle, angleDelta, radius )

        val turningLeft = angleDelta > 0f
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
        result = result  ::: List(pointData)
        result
    }

}