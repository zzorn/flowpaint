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

      // TODO: Collect values to the points as the values change along the stroke
      val startPoint: StrokePoint = points(i)
      val endPoint: StrokePoint = points(i + 1)

      val startX = startPoint.data.getProperty("x", 0)
      val startY = startPoint.data.getProperty("y", 0)
      val startAngle = startPoint.data.getProperty("angle", 0)
      val startRadius= startPoint.data.getProperty("radius", 1)
      val endX = endPoint.data.getProperty("x", 0)
      val endY = endPoint.data.getProperty("y", 0)
      val endAngle = endPoint.data.getProperty("angle", 0)
      val endRadius = endPoint.data.getProperty("radius", 1)

      StrokeRenderer.drawStrokeSegment(
        startX, startY, startAngle, startRadius,
        endX, endY, endAngle, endRadius,
        brush, surface)
    }

  }

}