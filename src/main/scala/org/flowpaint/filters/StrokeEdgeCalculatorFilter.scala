package org.flowpaint.filters

import property.{DataImpl, Data}
import util.{DataSample, PropertyRegister, MathUtils}
/**
 *  Calculates the edges of a stroke based on its radius and angle.
 *
 * @author Hans Haggstrom
 */

class StrokeEdgeCalculatorFilter extends PathProcessor {

    private val DefaultRadius = 10f
    private val RoundingSteps = 16
    private val RoundingAngle = 1f / 4f

    private var previousX = 0f
    private var previousY = 0f
    private var previousLeftX = 0f
    private var previousLeftY = 0f
    private var previousRightX = 0f
    private var previousRightY = 0f

    private var rightEdgeScale = 1f
    private var leftEdgeScale = 1f

    private var previousAngle = 0f

    private val AngleScale = 2f * Math.Pi.toFloat

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

    private def setCoordinates( pointData : Data, includeStartPoint : Boolean, radius : Float, x : Float, y : Float, leftX : Float, leftY : Float, rightX : Float, rightY: Float  )  {

        val epsilon = 0.1f   // TODO: May mess things up when extreme zooms or scalings or coordinates are used
        val x0l = if (includeStartPoint) x else MathUtils.interpolate( epsilon, x, leftX )
        val y0l = if (includeStartPoint) y else MathUtils.interpolate( epsilon, y, leftY )
        val x0r = if (includeStartPoint) x else MathUtils.interpolate( epsilon, x, rightX )
        val y0r = if (includeStartPoint) y else MathUtils.interpolate( epsilon, y, rightY )

        var newLeftX = leftX
        var newLeftY = leftY
        var newRightX = rightX
        var newRightY = rightY

        def intersect( x1 : Float, y1 : Float, x2 : Float, y2 :Float, px1 : Float, py1 : Float, px2 : Float, py2 :Float )  : Boolean = {
            
            util.GeometryUtils.isLineIntersectingLine(x1, y1, x2, y2, px1, py1, px2, py2)
        }

        // Check if either edge crosses the previous one.  In that case, use the previous endpoint
        // Do not apply for the first point
        if (!isFirstPoint ) {
            if ( intersect(x0l, y0l, leftX, leftY, previousX, previousY, previousLeftX, previousLeftY))
                {
                    newLeftX = previousLeftX
                    newLeftY = previousLeftY
                    leftEdgeScale = if (radius == 0) 0 else MathUtils.distance( x, y, newLeftX, newLeftY ) / radius
                }
            if ( intersect(x0r, y0r, rightX, rightY, previousX, previousY, previousRightX, previousRightY))
                {
                    newRightX = previousRightX
                    newRightY = previousRightY
                    rightEdgeScale = if (radius == 0) 0 else MathUtils.distance( x, y, newRightX, newRightY ) / radius
                }
        }

        pointData.setFloatProperty(PropertyRegister.PATH_X, x)
        pointData.setFloatProperty(PropertyRegister.PATH_Y, y)
        pointData.setFloatProperty(PropertyRegister.LEFT_EDGE_X, newLeftX)
        pointData.setFloatProperty(PropertyRegister.LEFT_EDGE_Y, newLeftY)
        pointData.setFloatProperty(PropertyRegister.RIGHT_EDGE_X, newRightX)
        pointData.setFloatProperty(PropertyRegister.RIGHT_EDGE_Y, newRightY)

        previousX =x
        previousY =y
        previousLeftX = newLeftX
        previousLeftY = newLeftY
        previousRightX = newRightX
        previousRightY = newRightY
    }


    private def roundCorner(pointData: Data, targetX : Float, targetY : Float, angle : Float, radius : Float) : List[Data] =  {

        val angleDelta : Float = MathUtils.wrappedClosestDelta( previousAngle / AngleScale, angle / AngleScale )

        var result : List[Data] = Nil

        val turningLeft = angleDelta > 0f

        val normPrevAngle = previousAngle / AngleScale
        val normNewAngle = angle / AngleScale

        val startX = previousX
        val startY = previousY

        // Add rounding points
        for ( i <- 1 to RoundingSteps) {

            val t : Float = (i.toFloat) / (RoundingSteps.toFloat)
            val stepAngle = MathUtils.wrappedInterpolate( t, normPrevAngle, normNewAngle )
            val invStepAngle = MathUtils.wrappedInterpolateLongerWay( t,  normPrevAngle, normNewAngle  )

            val x = MathUtils.interpolate( 0, startX, targetX )
            val y = MathUtils.interpolate( 0, startY, targetY )

            val data = new DataImpl( pointData )

            val cornerAngle = stepAngle * AngleScale
            val invCornerAngle = invStepAngle * AngleScale
            data.setFloatProperty(PropertyRegister.ANGLE, cornerAngle  )

           if ( i < RoundingSteps)
             pointData.setFloatProperty(PropertyRegister.ROUNDED_CORNER, 1)
           else
             pointData.setFloatProperty(PropertyRegister.ROUNDED_CORNER, 0)


            // Calculate corner points
            val leftAngle = if (!turningLeft) cornerAngle else invCornerAngle
            val rightAngle = if (turningLeft) cornerAngle else invCornerAngle

            var leftX = x - Math.cos(leftAngle).toFloat * radius * leftEdgeScale
            var leftY = y - Math.sin(leftAngle).toFloat * radius * leftEdgeScale

            var rightX = x + Math.cos(rightAngle).toFloat * radius * rightEdgeScale
            var rightY = y + Math.sin(rightAngle).toFloat * radius * rightEdgeScale

            setCoordinates(data, false, radius, x, y, leftX, leftY, rightX, rightY)

            result = result ::: List( data )

        }


        result
    }


    private def normalSegment( pointData: Data, x : Float, y : Float, angle : Float, radius : Float ) : List[Data] = {

        // Calculate corner points
        var leftX = x - Math.cos(angle).toFloat * radius * leftEdgeScale
        var leftY = y - Math.sin(angle).toFloat * radius * leftEdgeScale

        var rightX = x + Math.cos(angle).toFloat * radius * rightEdgeScale
        var rightY = y + Math.sin(angle).toFloat * radius * rightEdgeScale

        setCoordinates(pointData, true, radius, x, y, leftX, leftY, rightX, rightY)

        List(pointData)
    }

    private def recoverEdgeScales() {
        val recoverySpeed = getFloatProperty( "cornerScaleRecoverySpeed", 0.03f )
        leftEdgeScale = Math.min( 1f, leftEdgeScale + recoverySpeed )
        rightEdgeScale = Math.min( 1f, rightEdgeScale + recoverySpeed )
    }

    private def calculateTurnAmount( angle : Float ) : Float = {

      if (isFirstPoint) 0
      else {
        val normPrevAngle = previousAngle / AngleScale
        val normNewAngle = angle / AngleScale

        MathUtils.wrappedDistance( normPrevAngle, normNewAngle ) * 2
      }
    }

    private def shouldRound( turnAmount : Float ) : Boolean = {

        !isFirstPoint && turnAmount > RoundingAngle
    }

    protected def processPathPoint(pointData: Data) : List[Data] =  {

        val x = pointData.getFloatProperty(PropertyRegister.PATH_X, 0)
        val y = pointData.getFloatProperty(PropertyRegister.PATH_Y, 0)
        val angle = pointData.getFloatProperty(PropertyRegister.ANGLE, 0)
        val radius = pointData.getFloatProperty(PropertyRegister.RADIUS, DefaultRadius)

        val turnAmount = calculateTurnAmount( angle )
        pointData.setFloatProperty(PropertyRegister.TURN_AMOUNT, turnAmount)

        recoverEdgeScales()

        val result = if ( shouldRound( turnAmount ) )
                         roundCorner( pointData, x, y, angle, radius )
                     else
                         normalSegment( pointData, x, y, angle, radius )

        previousAngle = angle

        result
    }

}