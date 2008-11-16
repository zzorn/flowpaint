package org.flowpaint.model


import brush.Brush
import renderer.{PictureProvider, RenderSurface}
import renderer.StrokeRenderer
import scala.collection.jcl.ArrayList
import util.{DataSample, RectangleInt}

/**
 *   A brush stroke on some layer.
 *
 * @author Hans Haggstrom
 */
class Stroke(brush: Brush) extends PictureProvider {
  private val points : ArrayList[StrokePoint] = new ArrayList[StrokePoint]()

  def addPoint( data : DataSample  ) {
    points.add( new StrokePoint(data) )
  }


  def updateSurface(surface: RenderSurface) = {

    val startPoint: DataSample = new DataSample()
    val endPoint: DataSample =  new DataSample()

    if (points.length > 0)
      {
        endPoint.setValuesFrom(points(0).data)
      }

    for (i <- 0 until (points.length - 1)) {

      // Remember the variable values along the line even if they are only present
      // in the points when they have changed rom the previous value.
      startPoint.setValuesFrom( points(i).data )
      endPoint.setValuesFrom( points(i+1).data )
      
      val startX = startPoint.getProperty("x", 0)
      val startY = startPoint.getProperty("y", 0)
      val startAngle = startPoint.getProperty("angle", 0)
      val startRadius= startPoint.getProperty("radius", 1)
      val endX = endPoint.getProperty("x", 0)
      val endY = endPoint.getProperty("y", 0)
      val endAngle = endPoint.getProperty("angle", 0)
      val endRadius = endPoint.getProperty("radius", 10)

      StrokeRenderer.drawStrokeSegment(
        startX, startY, startAngle, startRadius,
        endX, endY, endAngle, endRadius,
        brush, surface)
    }

  }

}