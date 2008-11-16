package org.flowpaint.model


import brush.Brush
import filters.StrokeListener
import renderer.{PictureProvider, RenderSurface}
import renderer.StrokeRenderer
import scala.collection.jcl.ArrayList
import util.{DataSample, RectangleInt}

/**
 *    A brush stroke on some layer.
 *
 * @author Hans Haggstrom
 */
case class Stroke(brush: Brush) extends PictureProvider {
  private val points: ArrayList[DataSample] = new ArrayList[DataSample]()


  /**
   *  Adds a stroke point.  Doesn't update the picture.
   */
  def addPoint(data: DataSample) {
    points.add(data)
  }


  /**
   *  Adds a stroke point and updates the render surface with the latest stroke segment
   */
  def addPoint(data: DataSample, surface: RenderSurface) {

    addPoint(data)

    renderStroke(surface, false)

  }


  def updateSurface(surface: RenderSurface) = {

    renderStroke(surface, true)

  }


  private def renderStroke(surface: RenderSurface, allSegments: Boolean) {
    val startPoint: DataSample = new DataSample()
    val endPoint: DataSample = new DataSample()

    if (points.length > 0)
      {
        endPoint.setValuesFrom(points(0))
      }

    for (i <- 0 until (points.length - 1)) {

      // Remember the variable values along the line even if they are only present
      // in the points when they have changed rom the previous value.
      startPoint.setValuesFrom(points(i))
      endPoint.setValuesFrom(points(i + 1))

      if (allSegments || i == points.length - 2)
        renderStrokeSegment(startPoint, endPoint, surface)
    }

  }

  private def renderStrokeSegment(startPoint: DataSample, endPoint: DataSample, surface: RenderSurface) {
    val startX = startPoint.getProperty("x", 0)
    val startY = startPoint.getProperty("y", 0)
    val startAngle = startPoint.getProperty("angle", 0)
    val startRadius = startPoint.getProperty("radius", 1)
    val endX = endPoint.getProperty("x", 0)
    val endY = endPoint.getProperty("y", 0)
    val endAngle = endPoint.getProperty("angle", 0)
    val endRadius = endPoint.getProperty("radius", 10)

    StrokeRenderer.drawStrokeSegment(
      startX, startY, startAngle, startRadius,
      endX, endY, endAngle, endRadius,
      startPoint, endPoint,
      brush, surface)

  }
}