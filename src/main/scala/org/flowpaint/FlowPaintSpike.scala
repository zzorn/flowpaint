package org.flowpaint

import java.awt.{Dimension, Graphics, Color}
import javax.swing.{JPanel, JFrame, JLabel}
import scala.compat.Platform.currentTime

/**
 *                Spike of rendering 2D graphics with scala.
 *
 * @author Hans Haggstrom
 */
object FlowPaintSpike {

  // Various geometry utilities

  case class Point(x: Float, y: Float) {
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
     *                false if it is on the line or to the right of the line.
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
    def intersect(otherLine: Line): Point = {

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

        Point(x, y)
      }

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
    def iterate(segment: StrokeSegment, visitor: (Point) => Unit) {

      // Use segment bounding box to reudce the area needed to be iterated through
      val minX = segment.minX.toInt
      val minY = segment.minY.toInt
      val maxX = segment.maxX.toInt
      val maxY = segment.maxY.toInt

      for (y <- minY to maxY;
           x <- minX to maxX) visitor(Point(x, y))

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


  trait Brush {
    def calculateColor(stroke: StrokeSegment, positionAlongStroke: Float, centerDistance: Float): Int
  }

  /**
   *             Just a simple test brush.
   */
  class FixedSizeBrush() extends Brush {
    def calculateColor(stroke: StrokeSegment,
                      positionAlongStroke: Float,
                      centerDistance: Float): Int = {
      if (Math.abs(centerDistance) < 1 &&
              positionAlongStroke >= 0 &&
              positionAlongStroke <= 1)
        {
          val normalizedCenter = 1f - centerDistance

          val r = 1
          val g = 1 - (1 - positionAlongStroke) * normalizedCenter
          val b = 1 - normalizedCenter

          val red = (255 * r).toInt
          val green = (255 * g).toInt
          val blue = (255 * b).toInt
          val alpha = 255

          val color = ((alpha & 0xFF) << 24) |
                  ((red & 0xFF) << 16) |
                  ((green & 0xFF) << 8) |
                  ((blue & 0xFF) << 0);

          color
        }
      else
        0xFFFFFF
    }
  }


  def main(args: Array[String]) {

    val sizeX = 1000
    val sizeY = 700

    class DrawPanel extends JPanel {
      override def paintComponent(g: Graphics): Unit = {

        def putPixel(x: Int, y: Int, color: Int) {

          // NOTE: Naive unoptimized implementation, TODO: Optimized access to a bitmap
          g.setColor(new Color(color, false)) // NOTE: If we enable alpha color, rendering is _really_ slow
          g.drawLine(x, y, x, y)

        }




        def drawStrokeSegment(segment: StrokeSegment, area: Area, brush: Brush) {

          val length = segment.length

          //          if (length > 0)
          {
            val startPoint: Point = segment.start.point
            val endPoint: Point = segment.end.point

            val centerPoint = if (length == 0) startPoint else Line(startPoint, segment.start.angle) intersect
                    Line(endPoint, segment.end.angle)
            if (centerPoint != null)
              {
                val strokeLine = Line(startPoint, endPoint)

                def between(value: Float, min: Float, max: Float): Boolean = value < max && value >= min
                def interpolate(t: Float, a: Float, b: Float): Float = (1.0f - t) * a + t * b

                area iterate (segment, (point: Point) => {
                  val strokePos = if (length == 0) startPoint else Line(point, centerPoint) intersect strokeLine
                  if (strokePos != null)
                    {
                      val startToStrokePos = if (length == 0) 0.5f else (startPoint distance strokePos) / length
                      val endToStrokePos = if (length == 0) 0.5f else (endPoint distance strokePos) / length
                      val positionAlongStroke = if (endToStrokePos <= 1) startToStrokePos else 1 - endToStrokePos


                      val radius = interpolate(positionAlongStroke, segment.start.radius, segment.end.radius)
                      var centerDistance = point distance strokePos
                      if (radius == 0) centerDistance = 0.5f else centerDistance /= radius

                      if (Math.abs(centerDistance) <= 1 && between(positionAlongStroke, 0, 1))
                        {
                          // Multiply center distance with -1 if it is on the left side of the stroke
                          if (point leftOf strokeLine)
                            centerDistance = -centerDistance

                          val color = brush.calculateColor(segment, positionAlongStroke, centerDistance)

                          putPixel(point.x.toInt, point.y.toInt, color)

                        }
                    }
                })

              }
          }

        }

        def degrees(d: Double): Float = (d * Math.Pi / 180.0).toFloat


        val random = new scala.util.Random(42)
        val STROKES_PER_TEST_RUN = 10

        def runTest(): Long = {
          val startTime = currentTime
          def rnd(value: Int): Float = random.nextFloat * value

          val area = Area(0, 0, sizeX, sizeY)
          val fixedSizeBrush = new FixedSizeBrush()
          var segment: StrokeSegment = null

          for (i <- 0 until STROKES_PER_TEST_RUN)
            {
              segment = StrokeSegment(
                SegmentEnd(Point(rnd(sizeX), rnd(sizeY)), degrees(rnd(360)), rnd(80)),
                SegmentEnd(Point(rnd(sizeX), rnd(sizeY)), degrees(rnd(360)), rnd(80)))

              drawStrokeSegment(segment, area, fixedSizeBrush)

            }
          val testRunTime = currentTime - startTime
          println("  " + STROKES_PER_TEST_RUN + " strokes rendered in: " + testRunTime + " ms")
          testRunTime
        }

        var sum = 0L
        val TEST_RUNS = 3
        for (i <- 0 until TEST_RUNS) sum += runTest
        sum /= TEST_RUNS

        println("Used time on average per stroke " + sum / STROKES_PER_TEST_RUN + " ms")


      }
    }

    val drawPanel = new DrawPanel()

    drawPanel.setPreferredSize(new Dimension(sizeX, sizeY))

    val frame = new JFrame()
    frame.setContentPane(drawPanel)

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.pack
    frame.show

  }

}

