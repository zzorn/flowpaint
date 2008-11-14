package org.flowpaint

import brush.GradientTestBrush
import java.awt.{Dimension, Graphics, Color}
import javax.swing.{JPanel, JFrame, JLabel}
import renderer.StrokeRenderer.{StrokeSegment, SegmentEnd, Area, Point}
import renderer.{StrokeRenderer, RenderSurface}
import scala.compat.Platform.currentTime
import util.PerformanceTester

/**
 * Spike of rendering 2D graphics with scala.
 *
 * @author Hans Haggstrom
 */
object FlowPaintSpike {
  def main(args: Array[String]) {

    val sizeX = 1000
    val sizeY = 700


    class DrawPanel extends JPanel {
      override def paintComponent(g: Graphics): Unit = {

        val surface = new RenderSurface {
          def putPixel(x: Int, y: Int, color: Int) = {
            // NOTE: Naive unoptimized implementation, TODO: Optimized access to a bitmap
            g.setColor(new Color(color, false)) // NOTE: If we enable alpha color, rendering is _really_ slow
            g.drawLine(x, y, x, y)
          }
        }


        val random = new scala.util.Random(42)


        def testedFunction() = {
          def rnd(value: Int): Float = random.nextFloat * value
          def degrees(d: Double): Float = (d * Math.Pi / 180.0).toFloat

          val area = Area(0, 0, sizeX, sizeY)
          var segment: StrokeSegment = null

          segment = StrokeSegment(
            SegmentEnd( Point(rnd(sizeX), rnd(sizeY)), degrees(rnd(360)), rnd(80)),
            SegmentEnd( Point(rnd(sizeX), rnd(sizeY)), degrees(rnd(360)), rnd(80)))

          StrokeRenderer.drawStrokeSegment(segment, area, new GradientTestBrush(), surface)

        }

        PerformanceTester.timeFunction( 30, testedFunction )
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

