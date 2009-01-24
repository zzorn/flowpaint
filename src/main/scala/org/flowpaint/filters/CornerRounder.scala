package org.flowpaint.filters


import property.{DataImpl, Data}
import util.DataSample
import util.MathUtils
import Math.abs

import util.PropertyRegister
/**
 * 
 * 
 * @author Hans Haggstrom
 */

class CornerRounder extends PathProcessor {

    private var previousAngle = 0f

    override protected def onInit() {
        previousAngle = 0f
    }


    protected def processPathPoint(pointData: Data) : List[Data] =  {

        val angleScale = 2f * Math.Pi.toFloat
        val roundingAngle = 1f / 16f

        var result : List[Data] = Nil

        if (isFirstPoint) {
            previousAngle = pointData.getFloatProperty(PropertyRegister.ANGLE, 0f)
        }
        else {

            val angle = pointData.getFloatProperty(PropertyRegister.ANGLE, previousAngle)

            if ( angle != previousAngle ) {

                val normPrevAngle = previousAngle / angleScale
                val normNewAngle = angle / angleScale

                val distance = MathUtils.wrappedDistance( normPrevAngle, normNewAngle )

                if (distance > roundingAngle ) {

                    // Add rounding points
                    val steps = (distance / roundingAngle).toInt

                    for ( i <- 0 to steps) {

                        println( "step " + i)

                        val t : Float = (i.toFloat) / (steps.toFloat)
                        val stepAngle = MathUtils.wrappedInterpolate( t, normPrevAngle, normNewAngle )

                        println( "stepAngle " + stepAngle)

                        val data = new DataImpl( pointData )
                        data.setFloatProperty(PropertyRegister.ANGLE, stepAngle * angleScale )

                        result = result ::: List( data )
                    }

                }
            }

            previousAngle = angle
        }

        result = result ::: List( pointData )

        return result
    }



}