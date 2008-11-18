package org.flowpaint.ui

import brush.Brush
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

class BrushPreview( val brush :Brush )  extends JPanel {



  def defUpdateStroke()  {

    stroke.clear()

    val w = getWidth().toFloat
    val h = getHeight().toFloat

    for (i <- 0 until 10) {
      val f : Float= 1.0f * i / 10.0f

      val dataSample = new DataSample()
      dataSample.setProperty("index",i.toFloat )
      dataSample.setProperty("x",w *f )
      dataSample.setProperty("y",h * Math.sin(f).toFloat)
      dataSample.setProperty("pressure",1)
      dataSample.setProperty("time",f)
      stroke.addPoint( dataSample )
    }

    repaint()
  }



  private val stroke = new Stroke( brush )
  private val painting = new Painting()
  private val surface = new SingleRenderSurface( painting )
  private val paintPanel = new PaintPanel( surface )

  setLayout(new java.awt.BorderLayout())
  setPreferredSize(new Dimension( 32, 32 ))
  setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.BLACK, 1 ) )

  painting.currentLayer.addStroke( stroke )

  add(paintPanel, java.awt.BorderLayout.CENTER)

  defUpdateStroke()

  addComponentListener(new ComponentListener() {

    def componentMoved(e: java.awt.event.ComponentEvent ) {}
    def componentShown(e: java.awt.event.ComponentEvent ) { defUpdateStroke() }
    def componentHidden(e: java.awt.event.ComponentEvent ) {}
    def componentResized(e :java.awt.event.ComponentEvent ) { defUpdateStroke() }
  })

  private var mousePressedOnThisButton = false
  private val UNPRESSED_COLOR = new Color(230, 230, 230)
  private def press() {
    mousePressedOnThisButton = true
    painting.backgroundColor = java.awt.Color.WHITE
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
  addMouseListener( new MouseAdapter() {

    override def mousePressed(e: java.awt.event.MouseEvent ) {
      press()
    }
    override def mouseReleased(e: java.awt.event.MouseEvent ) {
      if (mousePressedOnThisButton){
        FlowPaintController.currentBrush = brush
        unpress()
      }
    }
    override def mouseExited(e: java.awt.event.MouseEvent ) {
      unpress()
    }

  })

}

