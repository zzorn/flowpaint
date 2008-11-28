package org.flowpaint.renderer

import brush.Brush
import java.awt.Color
import org.flowpaint.brush
import util.{DataSample, RectangleInt}
import util.MathUtils.squaredDistance

/**
 *     Renders a stroke segment.
 *
 * @author Hans Haggstrom
 */
object StrokeRenderer {
  private val TRANSPARENT_COLOR = new Color(0, 0, 0, 0).getRGB()


  /**
   *     Renders a segment of a stroke.  The segment has start and end coordinates, radius, and angles.
   */
  def drawStrokeSegment(startX: Float, startY: Float, startAngle: Float, startRadius: Float,
                       endX: Float, endY: Float, endAngle: Float, endRadius: Float,
                       startData: DataSample, endData: DataSample,
                       brush: Brush, surface: RenderSurface) {

    val squaredLength = squaredDistance(startX, startY, endX, endY)

    if (squaredLength > 0) {

      val length = Math.sqrt(squaredLength).toFloat

      // Used to store pixel specific properties in
      val pixelData = new DataSample()

      // Calculate a bounding box for the stroke segment
      val minX: Float = Math.min(startX - startRadius, endX - endRadius)
      val minY: Float = Math.min(startY - startRadius, endY - endRadius)
      val maxX: Float = Math.max(startX + startRadius, endX + endRadius)
      val maxY: Float = Math.max(startY + startRadius, endY + endRadius)

      // Calculate points one unit along in the direction of the start and end angles
      val startAngleOffsetX = Math.cos(startAngle).toFloat
      val startAngleOffsetY = Math.sin(startAngle).toFloat
      val startX2 = startX + startAngleOffsetX
      val startY2 = startY + startAngleOffsetY
      val endX2 = endX + Math.cos(endAngle).toFloat
      val endY2 = endY + Math.sin(endAngle).toFloat

      // Calculate the point at which the start and end angle converge, or wether the angles are parallel
      val centerPoint = Point(0, 0)
      val fixedCenterpoint = intersect(startX, startY, startX2, startY2,
        endX, endY, endX2, endY2, centerPoint)

      val strokePos = Point(0, 0)

      surface.provideContent(minX, minY, maxX, maxY, (x: Int, y: Int) => {
        // Default result color
        var color = TRANSPARENT_COLOR;

        // If the angles were parallel, we have to move the center point continuously relative to the query point
        if (!fixedCenterpoint) {
          centerPoint.x = x + startAngleOffsetX
          centerPoint.y = y + startAngleOffsetY
        }

        // Get the point along the stroke that this pixel maps to (depends on the local brush angle)
        val strokeIntersectionFound = intersect(x, y, centerPoint.x, centerPoint.y,
          startX, startY, endX, endY,
          strokePos)

        if (strokeIntersectionFound)
          {
            val startToStrokePosSquared = squaredDistance(startX, startY, strokePos.x, strokePos.y) / squaredLength
            val endToStrokePosSquared = squaredDistance(endX, endY, strokePos.x, strokePos.y) / squaredLength

            // Check that the current pixel maps to between the segment start and endpoint
            if (startToStrokePosSquared <= 1 && endToStrokePosSquared <= 1)
              {
                val positionAlongStroke = Math.sqrt(startToStrokePosSquared).toFloat

                val radius = util.MathUtils.interpolate(positionAlongStroke, startRadius, endRadius)
                val radiusSquared = radius * radius

                var centerDistanceSquared = squaredDistance(x, y, strokePos.x, strokePos.y)

                // Check that the current pixel is within the correct radius from the segment
                if (centerDistanceSquared <= radiusSquared && radius > 0)
                  {
                    val relativeCenterDistance = Math.sqrt(centerDistanceSquared).toFloat / radius

                    // Give across a sign depending on which side of the stroke the point is
                    val positionAcrossStroke = if (util.MathUtils.rightOf(startX, startY, endX, endY, x, y))
                      relativeCenterDistance
                    else
                      -relativeCenterDistance

                    pixelData.clear()

                    pixelData.setValuesFrom( startData )
                    pixelData.interpolate( positionAlongStroke, endData )
                    pixelData.setProperty( "positionAlongStroke", positionAlongStroke )
                    pixelData.setProperty( "positionAcrossStroke", positionAcrossStroke )

                    brush.ink.calculateColor( pixelData )

                    color = util.ColorUtils.createRGBAColor(
                      pixelData.getProperty("red",0),
                      pixelData.getProperty("green",0),
                      pixelData.getProperty("blue",0),
                      pixelData.getProperty("alpha",0))
                  }

              }

          }

        // Return calculated color
        color
      })


    }


  }


  /**
   *      Simple helper class to hold a coordinate pair.
   */
  private case class Point(var x: Float, var y: Float)

  /**
   * @param intersectionOut the point to store the intersection point between the lines to.
   * @return true if an intersection was found, false if the lines are parallel
   *            (can be either no intersection, or a line intersection)
   */
  private def intersect(x1: Float, y1: Float,
                       x2: Float, y2: Float,
                       x3: Float, y3: Float,
                       x4: Float, y4: Float,
                       intersectionOut: Point): Boolean = {

    // TODO: Inline to optimize?  Could also pre-calculate the values for one of the lines, as one of them is in the same place
    def det(a: Float, b: Float, c: Float, d: Float): Float =
      {
        a * d - b * c;
      }


    val det3: Float = det(x1 - x2, y1 - y2, x3 - x4, y3 - y4)

    if (det3 == 0)
      false // Parallel lines TODO: This is an assumption, as there would be a divide by zero otherwise
    else {
      val det1: Float = det(x1, y1, x2, y2)
      val det2: Float = det(x3, y3, x4, y4)

      val x = det(det1, x1 - x2, det2, x3 - x4) / det3;
      val y = det(det1, y1 - y2, det2, y3 - y4) / det3;

      intersectionOut.x = x
      intersectionOut.y = y

      true
    }

  }


}