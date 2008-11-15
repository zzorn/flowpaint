package org.flowpaint.model


import brush.Brush
import renderer.{PictureProvider, RenderSurface}
import renderer.StrokeRenderer
import scala.collection.jcl.ArrayList
import util.RectangleInt

/**
 *   A brush stroke on some layer.
 *
 * @author Hans Haggstrom
 */
class Stroke(brush: Brush) extends PictureProvider {
  val points : ArrayList[StrokePoint] = new ArrayList[StrokePoint]()

  def updateSurface(surface: RenderSurface) = {

    for (i <- 0 until (points.length - 1)) {

      println ("Stroke rendering point "+i+" to "+(i+1))

      // TODO: Collect values to the points as the values change along the stroke
      val startPoint: StrokePoint = points(i)
      val endPoint: StrokePoint = points(i + 1)

      val startX = startPoint.getProperty("x", 0)
      val startY = startPoint.getProperty("y", 0)
      val startAngle = startPoint.getProperty("angle", 0)
      val startRadius= startPoint.getProperty("radius", 1)
      val endX = endPoint.getProperty("x", 0)
      val endY = endPoint.getProperty("y", 0)
      val endAngle = endPoint.getProperty("angle", 0)
      val endRadius = endPoint.getProperty("radius", 1)

      println("startX:"+startX)
      println("endX:"+endX)
      println("startR:"+startRadius)
      println("endR:"+endRadius)
      println("startA:"+startAngle)
      println("endA:"+endAngle)

      StrokeRenderer.drawStrokeSegment(
        startX, startY, startAngle, startRadius,
        endX, endY, endAngle, endRadius,
        brush, surface)
    }

  }

}