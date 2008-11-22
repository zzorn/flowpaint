package org.flowpaint.ui

import brush.Brush
import filters.StrokeListener
import java.awt.event.{ComponentListener, MouseAdapter}
import java.awt.{Graphics2D, Dimension, Graphics, Color}
import javax.swing.JPanel
import model.{Stroke, Painting}
import renderer.SingleRenderSurface
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

class BrushPreview(val brush: Brush) extends JPanel {
  def updateStroke() {

    stroke.clear()

    val w = getWidth().toFloat
    val h = getHeight().toFloat

    val STEPS = 10

    def generatePoint(i: Int) {
      val f: Float = (1f * i) / (1f * STEPS)

      // TODO: Use nicer smoother curves later

      val pressure = 0.5f + 0.5f*Math.cos( 2*Math.Pi * f + Math.Pi ).toFloat

      val dataSample = new DataSample()
      dataSample.setProperty("index", i.toFloat)
      dataSample.setProperty("x", w * f)
      dataSample.setProperty("y", h * f)
      dataSample.setProperty("pressure", pressure) //1f - Math.abs((1f - f * 2))
      dataSample.setProperty("time", f * 0.5f)


      // Run the input point through the filters in the stroke
      /*
            stroke.filterAndAdd( dataSample )
      */
      // TODO: Change to use functions instead of anonymous one method classes
      stroke.brush.filterStrokePoint(dataSample, new StrokeListener() {
        def addStrokePoint(pointData: DataSample) {
          stroke.addPoint(pointData)
        }
      })

    }

    generatePoint(-1) // NOTE: Generating one initial point, as some of the filters remove initial points.  Fix filters..
    for (i <- 0 to STEPS) {
      generatePoint(i)
    }
    
    repaint()
  }


  private val stroke = new Stroke(brush)
  private val painting = new Painting()
  private val surface = new SingleRenderSurface(painting)
  private val paintPanel = new PaintPanel(surface, false)

  setLayout(new java.awt.BorderLayout())
  setPreferredSize(new Dimension(32, 32))
  setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1))

  painting.currentLayer.addStroke(stroke)

  add(paintPanel, java.awt.BorderLayout.CENTER)

  updateStroke()

  addComponentListener(new ComponentListener() {
    def componentMoved(e: java.awt.event.ComponentEvent) {}

    def componentShown(e: java.awt.event.ComponentEvent) {}

    def componentHidden(e: java.awt.event.ComponentEvent) {}

    def componentResized(e: java.awt.event.ComponentEvent) {updateStroke()}
  })

  private var mousePressedOnThisButton = false
  private val UNPRESSED_COLOR = new Color(230,230,230)
  private val PRESSED_COLOR=  new Color(250,210,100)

  private def press() {
    mousePressedOnThisButton = true
    painting.backgroundColor = PRESSED_COLOR
    surface.updateSurface()
    repaint()
  }

  private def unpress() {
    mousePressedOnThisButton = false
    painting.backgroundColor = UNPRESSED_COLOR
    surface.updateSurface()
    repaint()
  }

  unpress()
  addMouseListener(new MouseAdapter() {
    override def mousePressed(e: java.awt.event.MouseEvent) {
      FlowPaintController.currentBrush = brush
      press()
    }

    override def mouseReleased(e: java.awt.event.MouseEvent) {
      if (mousePressedOnThisButton) {
        unpress()
      }
    }

    override def mouseExited(e: java.awt.event.MouseEvent) {
      unpress()
    }

  })


}