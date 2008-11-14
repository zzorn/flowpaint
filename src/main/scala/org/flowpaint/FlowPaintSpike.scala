package org.flowpaint

import java.awt.{Dimension, Graphics, Color}
import javax.swing.{JPanel, JFrame, JLabel}

/**
 *             Spike of rendering 2D graphics with scala.
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

    /**
     * @return true if this point is to the left of the specified line, seen along the direction of the line,
     *             false if it is on the line or to the right of the line.
     */
    def leftOf(line: Line): Boolean =
      {
        // TODO: Implement
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
    def iterate(visitor: (Point) => Unit) {
      for (x <- x1 to x2;
           y <- y1 to y2) visitor(Point(x, y))

    }
  }



  case class SegmentEnd(point: Point, angle: Float)

  case class StrokeSegment(start: SegmentEnd, end: SegmentEnd) {
    def length: Float = start.point distance end.point

  }


  trait Brush {
    def calculateColor(stroke: StrokeSegment, positionAlongStroke: Float, centerDistance: Float): Int
  }

  /**
   *          Just a simple test brush.
   */
  class FixedSizeBrush(radius: Float) extends Brush {
    def calculateColor(stroke: StrokeSegment,
                      positionAlongStroke: Float,
                      centerDistance: Float): Int = {
      if (Math.abs(centerDistance) < radius &&
              positionAlongStroke >= 0 &&
              positionAlongStroke <= 1)
        {
          val normalizedCenter = 1f - Math.abs(centerDistance / radius)

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

    class DrawPanel extends JPanel {
      override def paintComponent(g: Graphics): Unit = {

        def putPixel(x: Int, y: Int, color: Int) {

          // NOTE: Naive unoptimized implementation, TODO: Optimized access to a bitmap
          g.setColor(new Color(color, false)) // NOTE: If we enable alpha color, rendering is _really_ slow
          g.drawLine(x, y, x, y)

        }




        def drawStrokeSegment(segment: StrokeSegment, area: Area, brush: Brush) {

          val length = segment.length

          if (length > 0)
            {
              val startPoint: Point = segment.start.point
              val endPoint: Point = segment.end.point

              val centerPoint = Line(startPoint, segment.start.angle) intersect
                      Line(endPoint, segment.end.angle)
              if (centerPoint != null)
                {
                  val strokeLine = Line(startPoint, endPoint)

                  area iterate {
                    point: Point => {

                      val strokePos = Line(point, centerPoint) intersect strokeLine
                      if (strokePos != null)
                        {
                          val startToStrokePos = (startPoint distance strokePos) / length
                          val endToStrokePos = (endPoint distance strokePos) / length
                          val positionAlongStroke = if (endToStrokePos <= 1) startToStrokePos else 1 - endToStrokePos


                          var centerDistance = point distance strokePos

                          // TODO: Calculate the interpolated stroke radius, and normalize the center distance value too..
                          // This means adding brush size to segment endpoints, and removing it from the brush.

                          // Multiply center distance with -1 if it is on the left side of the stroke
                          if (point leftOf strokeLine)
                            centerDistance = -centerDistance

                          val color = brush.calculateColor(segment, positionAlongStroke, centerDistance)

                          putPixel(point.x.toInt, point.y.toInt, color)
                        }
                    }
                  }
                }
            }

        }

        def degrees(d: Double): Float = (d * Math.Pi / 180.0).toFloat

        def rnd(value : Int):Float = (Math.random * value).toFloat

        val sizeX = 500
        val sizeY = 500
        val area = Area(0, 0, sizeX, sizeY)
        val fixedSizeBrush = new FixedSizeBrush(20)
        var segment : StrokeSegment= null
        for (i <- 0 to 3)
          {
            segment = StrokeSegment(
              SegmentEnd(Point(rnd(sizeX), rnd (sizeY)), degrees(rnd (360))),
              SegmentEnd(Point(rnd (sizeX), rnd (sizeY)), degrees(rnd (360))))

            drawStrokeSegment(segment, area, fixedSizeBrush)

          }


      }
    }

    val drawPanel = new DrawPanel()

    drawPanel.setPreferredSize(new Dimension(800, 600))

    val frame = new JFrame()
    frame.setContentPane(drawPanel)

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.pack
    frame.show

  }

}

