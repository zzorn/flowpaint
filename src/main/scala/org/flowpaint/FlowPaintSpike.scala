package org.flowpaint

import java.awt.{Dimension, Graphics}
import javax.swing.{JPanel, JFrame, JLabel}

/**
 *       Spike of rendering 2D graphics with scala.
 *
 * @author Hans Haggstrom
 */

object FlowPaintSpike {
  def main(args: Array[String]) {

    class DrawPanel extends JPanel {
      override def paintComponent(g: Graphics): Unit = {

        def putPixel(x: Int, y: Int) {
          g.drawLine(x, y, x, y)

        }

        case class Area(x1: Int, y1: Int, x2: Int, y2: Int) {
          def iterate(visitor: (Int, Int) => Unit) {
            for (x <- x1 to x2;
                 y <- y1 to y2) visitor(x, y)

          }
        }

        case class Point(x: Float, y: Float)
        case class SegmentEnd(point: Point, angle: Float)

        def drawStrokeSegment(start: SegmentEnd, end: SegmentEnd, area: Area) {

          area iterate {(x: Int, y: Int) => putPixel(x, y)}

        }

        drawStrokeSegment(SegmentEnd(Point(50, 200), 50),
          SegmentEnd(Point(5000, 300), 20),
          Area(100, 100, 400, 400))

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

