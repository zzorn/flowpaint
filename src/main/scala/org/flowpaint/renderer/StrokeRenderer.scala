package org.flowpaint.renderer

import brush.Brush
import org.flowpaint.brush

/**
 *        Renders a stroke segment.
 *
 * @author Hans Haggstrom
 */
object StrokeRenderer {
  case class Point(var x: Float, var y: Float) {
    def distance(otherPoint: Point): Float = {
      val xDiff = x - otherPoint.x
      val yDiff = y - otherPoint.y
      Math.sqrt(xDiff * xDiff + yDiff * yDiff).toFloat
    }

    def angleTo(otherPoint: Point): Float = {
      if (x == otherPoint.x &&
              y == otherPoint.y)
        0
      else {
        Math.atan2(otherPoint.y - y, otherPoint.x - x).toFloat
      }

    }

    /**
     * @return true if this point is to the left of the specified line, seen along the direction of the line,
     *                       false if it is on the line or to the right of the line.
     */
    def leftOf(line: Line): Boolean =
      {
        // TODO: Implement, so that we can have non-symmetrical brushes
        false
      }
  }

  case class Line(start: Point, end: Point) {
    def x1 = start.x

    def y1 = start.y

    def x2 = end.x

    def y2 = end.y

    /**
     * @return the intersection point between this line and another line, or null if they are parallel
     */
    def intersect(otherLine: Line, intersectionOut: Point) = {

      def det(a: Float, b: Float, c: Float, d: Float): Float =
        {
          a * d - b * c;
        }

      val x3 = otherLine.x1
      val y3 = otherLine.y1
      val x4 = otherLine.x2
      val y4 = otherLine.y2

      val det3: Float = det(x1 - x2, y1 - y2, x3 - x4, y3 - y4)

      if (det3 == 0)
        null // Parallel lines TODO: This is an assumption, as there would be a divide by zero otherwise
      else {
        val det1: Float = det(x1, y1, x2, y2)
        val det2: Float = det(x3, y3, x4, y4)

        val x = det(det1, x1 - x2, det2, x3 - x4) / det3;
        val y = det(det1, y1 - y2, det2, y3 - y4) / det3;

        intersectionOut.x = x
        intersectionOut.y = y
      }

    }

  }


  /**
   * @param intersectionOut the point to store the intersection point between the lines to.
   * @return true if an intersection was found, false if the lines are parallel
   *       (can be either no intersection, or a line intersection)
   */
  def intersect(x1: Float, y1: Float,
               x2: Float, y2: Float,
               x3: Float, y3: Float,
               x4: Float, y4: Float,
               intersectionOut: Point): Boolean = {

    // OPTIMIZE: Inline?  Could also pre-calculate the values for one of the lines, as one of them is in the same place
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



  object Line {
    def apply(p: Point, angle: Float): Line = {

      // Calculate another point one unit along in the direction of the angle
      val x = p.x + Math.cos(angle).toFloat
      val y = p.y + Math.sin(angle).toFloat

      Line(p, Point(x, y))
    }
  }

  case class Area(x1: Int, y1: Int, x2: Int, y2: Int) {
    def iterate(segment: StrokeSegment, visitor: (Int, Int) => Unit) {

      // Use segment bounding box to reudce the area needed to be iterated through
      val minX = segment.minX.toInt
      val minY = segment.minY.toInt
      val maxX = segment.maxX.toInt
      val maxY = segment.maxY.toInt

      for (y <- minY to maxY;
           x <- minX to maxX) visitor(x, y)

    }
  }



  case class SegmentEnd(point: Point, angle: Float, radius: Float)

  case class StrokeSegment(start: SegmentEnd, end: SegmentEnd) {
    def length: Float = start.point distance end.point

    def minX: Float = Math.min(start.point.x - start.radius, end.point.x - end.radius)

    def minY: Float = Math.min(start.point.y - start.radius, end.point.y - end.radius)

    def maxX: Float = Math.max(start.point.x + start.radius, end.point.x + end.radius)

    def maxY: Float = Math.max(start.point.y + start.radius, end.point.y + end.radius)
  }





  def between(value: Float, min: Float, max: Float): Boolean = value < max && value >= min

  def interpolate(t: Float, a: Float, b: Float): Float = (1.0f - t) * a + t * b

  def squaredDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float = {
    val xDiff = x2 - x1
    val yDiff = y2 - y1
    xDiff * xDiff + yDiff * yDiff
  }

  def drawStrokeSegment(segment: StrokeSegment, area: Area, brush: Brush, surface: RenderSurface) {

    val length = segment.length
    val squaredLength = length * length

    if (length > 0) {

      val startAngle: Float = segment.start.angle
      val endAngle: Float = segment.end.angle

      val startX = segment.start.point.x
      val startY = segment.start.point.y
      val endX = segment.end.point.x
      val endY = segment.end.point.y

      val startRadius: Float = segment.start.radius
      val endRadius: Float = segment.end.radius

      val centerPoint = Point(startX, startY)

      // Calculate points one unit along in the direction of the start and end angles
      val segmentStart2x = startX + Math.cos(startAngle).toFloat
      val segmentStart2y = startY + Math.sin(startAngle).toFloat
      val segmentEnd2x = endX + Math.cos(endAngle).toFloat
      val segmentEnd2y = endY + Math.sin(endAngle).toFloat

      val intersectionFound = intersect(startX, startY, segmentStart2x, segmentStart2y,
        endX, endY, segmentEnd2x, segmentEnd2y, centerPoint)

      if (intersectionFound)
        {
          //val strokeLine = Line(startPoint, endPoint)

          val strokePos = Point(0, 0)

          area iterate (segment, (x: Int, y: Int) => {

            val strokeIntersectionFound = intersect(x, y, centerPoint.x, centerPoint.y,
              startX, startY, endX, endY,
              strokePos)

            if (strokeIntersectionFound)
              {
                val startToStrokePosSquared = squaredDistance(startX, startY, strokePos.x, strokePos.y) / squaredLength
                val endToStrokePosSquared = squaredDistance(endX, endY, strokePos.x, strokePos.y) / squaredLength

                if (startToStrokePosSquared <= 1 && endToStrokePosSquared <= 1)
                  {
                    val positionAlongStroke = Math.sqrt(startToStrokePosSquared).toFloat

                    val radius = interpolate(positionAlongStroke, startRadius, endRadius)
                    val radiusSquared = radius * radius

                    var centerDistanceSquared = squaredDistance(x, y, strokePos.x, strokePos.y)

                    if (centerDistanceSquared <= radiusSquared && radius > 0)
                      {
                        val positionAcrossStroke = Math.sqrt(centerDistanceSquared).toFloat / radius

                        /* TODO: Give across a sign depending on which side of the stroke the point is
                          // Multiply center distance with -1 if it is on the left side of the stroke
                          if (point leftOf strokeLine)
                            positionAcrossStroke = -positionAcrossStroke
                        */

                        val color = brush.calculateColor(positionAlongStroke, positionAcrossStroke)

                        surface.putPixel(x, y, color)

                      }

                  }

              }
          })

        }

    }


  }

  def degrees(d: Double): Float = (d * Math.Pi / 180.0).toFloat


}