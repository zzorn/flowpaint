package org.flowpaint.ui.slider

import _root_.org.flowpaint.brush.{Brush, BrushProperty}
import _root_.org.flowpaint.ink.Ink
import _root_.org.flowpaint.util.DataSample
import java.awt.{Graphics2D, Graphics, Color}
import javax.swing.{JPanel, JComponent}
import property.Data

/**
 * 
 *
 * @author Hans Haggstrom
 */

class InkSliderUi(editedData: Data,
                   description: String,
                   property: String,
                   min: Float,
                   max: Float,
                   inks : List[Ink] )
        extends SliderUi(editedData, description, property, min, max )  {

  private var background : JPanel = null

  protected def updateUi() {
    background.repaint()
  }

  protected def createBackground(indicatorPainter: (Graphics2D) => Unit): JComponent = {

    background = new JPanel() {
      override def paintComponent(g: Graphics) {

        def calculateNormalizedCoordinate(pos: Int, maxPos: Int): Float = {
          if (maxPos <= 1) 0.5f else (1.0f * pos) / (1.0f * (maxPos - 1))
        }

        val g2: Graphics2D = g.asInstanceOf[Graphics2D]

        val sample = new DataSample()

        val h = getHeight()
        val w = getWidth()
        var ny = 0f
        var nx = 0f
        var y = 0
        while (y < h) {
          ny = calculateNormalizedCoordinate(y, h)

          var x = 0
          while (x < w) {
            nx = calculateNormalizedCoordinate(x, w)

            sample.clear()
            sample.setProperty("screenX", x)
            sample.setProperty("screenY", y)
            sample.setProperty("normalizedX", nx)
            sample.setProperty("normalizedY", ny)

            val n = if (isVertical) ny else nx
            sample.setProperty(property, util.MathUtils.interpolate(n, min, max))

            inks.foreach((ink: Ink) => {
              ink.processPixel(sample)
            })

            // TODO: Do something **faster**..  Use surface?
            val color = new Color(sample.getProperty("red", 0), sample.getProperty("green", 0), sample.getProperty("blue", 0))
            g2.setColor(color)
            g2.fillRect(x, y, 1, 1)

            x += 1
          }

          y += 1
        }


        indicatorPainter(g2)
      }

    }

    background


  }
}