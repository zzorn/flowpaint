package org.flowpaint.gradient

import _root_.org.flowpaint.property.{Data, DataImpl}
import _root_.scala.xml.{Elem, Node}
import java.awt.Color
import java.util.ArrayList


import util.DataSample

case class GradientPoint(var position: Float, data: Data) extends Comparable[GradientPoint] {
    def compareTo(p1: GradientPoint): Int = {
        if (position < p1.position) -1
        else if (position > p1.position) 1
        else 0
    }

    def toXML() : Elem = { <gradientPoint position={position.toString}>{data.toXML()}</gradientPoint> }
}

object MultiGradient {
    def fromXML(node: Node): Gradient = {

        val identifier = (node \ "@name").text

        val gradient = new MultiGradient(identifier)

        val points = node \ "gradientPoint"
        points foreach ((pointNode: Node) => {
            val pos = (pointNode \ "@position").text.toFloat
            val data = Data.fromXML(pointNode)
            gradient.addPoint(new GradientPoint(pos, data))
        })

        gradient
    }
}

/**
 *    A Gradient with multiple datapoints
 *
 * @author Hans Haggstrom
 */
class MultiGradient(val identifier: String) extends Gradient {
    def this(identifier: String, points: GradientPoint*) {
        this (identifier)

        points.foreach(addPoint)
    }

    private val myPoints = new ArrayList[GradientPoint]()
    private val tempSearchPoint = new GradientPoint(0, new DataImpl())


    protected def gradientValue(zeroToOne: Float): Data = {

        def searchForValueIndex(value: Float): Int =
            {
                tempSearchPoint.position = value
                return java.util.Collections.binarySearch(myPoints, tempSearchPoint);
            }

        val output = new DataImpl()

        val pointsSize = myPoints.size()

        if (pointsSize == 0)
            {
                // No values, return an empty sample
            }
        else if (pointsSize == 1)
            {
                // Only one point, return its value
                output.setValuesFrom(myPoints.get(0).data)
            }
        else
            {
                val pointIndex = searchForValueIndex(zeroToOne)
                if (pointIndex >= 0)
                    {
                        // Found exact value match, return that point
                        output.setValuesFrom(myPoints.get(pointIndex).data)
                    }
                else
                    {
                        // Search returns -insertionPoint - 1 if the exact value was not found,
                        // calculate the insertion point
                        val insertionPointIndex = -(pointIndex + 1)

                        if (insertionPointIndex == pointsSize)
                            {
                                // Last point is closest
                                output.setValuesFrom(myPoints.get(pointsSize - 1).data)
                            }
                        else if (insertionPointIndex == 0)
                            {
                                // First color is closest
                                output.setValuesFrom(myPoints.get(0).data)
                            }
                        else
                            {
                                val startPoint = myPoints.get(insertionPointIndex - 1)
                                val endPoint = myPoints.get(insertionPointIndex)
                                val startValue = startPoint.position
                                val endValue = endPoint.position

                                val relativePosition = (zeroToOne - startValue) / (endValue - startValue)

                                output.setValuesFrom(startPoint.data)
                                output.interpolate(relativePosition, endPoint.data)
                            }
                    }
            }

        return output
    }

    /**
     *    Removes all points from this gradient.
     */
    def clearPoints()
        {
            myPoints.clear();
        }


    /**
     *    Adds the specified ColorGradientPoint.
     *
     * @param addedColorGradientPoint should not be null or already added.
     */
    def addPoint(point: GradientPoint)
        {
            myPoints.add(point)

            java.util.Collections.sort(myPoints)
        }


    /**
     *    Removes the specified ColorGradientPoint.
     *
     * @param removedColorGradientPoint should not be null.
     */
    def removePoint(point: GradientPoint)
        {
            myPoints.remove(point);
        }


    def toXML() : Elem = <gradient>{scala.collection.jcl.Conversions.convertList( myPoints) foreach ( _.toXML() ) }</gradient>
}



