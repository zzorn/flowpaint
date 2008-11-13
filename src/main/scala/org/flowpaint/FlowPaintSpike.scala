package org.flowpaint

import java.awt.{Dimension, Graphics, Color}
import javax.swing.{JPanel, JFrame, JLabel}

/**
 * Spike of rendering 2D graphics with scala.
 *
 * @author Hans Haggstrom
 */
object FlowPaintSpike {

  // Various geometry utilities

  case class Point(x: Float, y: Float) {
    def distance(otherPoint: Point): Float = {
      // TODO: Calculate distance
      0
    }
  }

  case class Line(position: Point, angle: Float) {
    def intersect(otherLine: Line): Point = {
      if (otherLine.angle == angle) {
        null
      }
      else {
        // TODO: Calculate intersection point
        Point(0,0)

      }
    }
  }

  object Line {
    def apply(a: Point, b: Point): Line = {
      // TODO: Calculate angle from a to b
      val angle = 0.0f
      new Line(a, angle)
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
   * Just a simple test brush.
   */
  class FixedSizeBrush( radius : Float ) extends Brush{
    def calculateColor(stroke: StrokeSegment,
                      positionAlongStroke: Float,
                      centerDistance: Float): Int = {
      if (Math.abs(centerDistance) < radius)
        0
      else
        0xFFFFFF
    }
  }


  def main(args: Array[String]) {

    class DrawPanel extends JPanel {
      override def paintComponent(g: Graphics): Unit = {

        def putPixel(x: Int, y: Int, color: Int) {

          // NOTE: Naive unoptimized implementation, TODO: Optimized access to a bitmap
          g.setColor(new Color(color))
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

              val strokeLine = Line(startPoint, endPoint)

              area iterate {
                point: Point => {

                  val strokePos = Line(point, centerPoint) intersect strokeLine

                  val positionAlongStroke = (startPoint distance strokePos) / length
                  val centerDistance = point distance strokePos

                  // TODO: Calculate the interpolated stroke radius, and normalize the center distance value too..
                  // This means adding brush size to segment endpoints, and removing it from the brush. 

                  val color = brush.calculateColor(segment, positionAlongStroke, centerDistance)

                  putPixel(point.x.toInt, point.y.toInt, color)
                }
              }
            }

        }

        val segment = StrokeSegment(SegmentEnd(Point(50, 200), 50),
          SegmentEnd(Point(5000, 300), 20))

        drawStrokeSegment(segment, Area(10, 10, 400, 400), new FixedSizeBrush(50))

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

