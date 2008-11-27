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
 * A JComponent that renders a view of the specified stroke points with the specified brush.
 *
 * @author Hans Haggstrom
 */
class BrushPreview(val brush: Brush, val strokePointCalculator : ( Float, Float, Float, DataSample ) => Unit  ) extends JPanel {

  if (brush== null) throw new IllegalArgumentException("brush should not be null")
  if (strokePointCalculator == null) throw new IllegalArgumentException("strokePointCalculator should not be null")

  private val stroke = new Stroke(brush)
  private val painting = new Painting()
  private val surface = new SingleRenderSurface(painting)
  private val paintPanel = new PaintPanel(surface, false)

  setLayout(new java.awt.BorderLayout())
  setPreferredSize(new Dimension(32, 32))
  setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1))

  painting.currentLayer.addStroke(stroke)

  add(paintPanel, java.awt.BorderLayout.CENTER)

  update()

  addComponentListener(new ComponentListener() {
    def componentMoved(e: java.awt.event.ComponentEvent) {}
    def componentShown(e: java.awt.event.ComponentEvent) {}
    def componentHidden(e: java.awt.event.ComponentEvent) {}
    def componentResized(e: java.awt.event.ComponentEvent) {update()}
  })

  // Update the stroke when the brush changes
  brush.addChangeListener( { b => update() } )

  
  /**
   * Call this if the preview stroke should be re-rendered.
   */
  def update() {

    stroke.clear()

    val w = getWidth().toFloat
    val h = getHeight().toFloat

    val STEPS = 10

    def generatePoint(i: Int) {
      val f: Float = (1f * i) / (1f * STEPS)

      val dataSample = new DataSample()
      dataSample.setProperty("index", i.toFloat)

      strokePointCalculator( f, w, h, dataSample )

      // Run the input point through the filters in the stroke
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

    surface.updateSurface()

    repaint()
  }

  def setBackgroundColor( color :Color ) {
    painting.backgroundColor = color

    repaint()
  }



}