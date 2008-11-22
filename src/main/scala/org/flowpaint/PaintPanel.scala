package org.flowpaint

import java.awt._
import java.awt.event.{ComponentListener, ComponentEvent}
import java.awt.image.BufferedImage
import javax.swing.JPanel
import renderer.RenderSurface
/**
 * A swing component that can be painted on.
 *
 * @author Hans Haggstrom
 */
class PaintPanel( surface: RenderSurface, useCrosshairCursor : Boolean ) extends JPanel {

  val THIN_CROSSHAIR_CURSOR =  createThinCrosshairCursor()

  addComponentListener( new ComponentListener{
    def componentMoved(e :ComponentEvent ) = {}
    def componentShown(e :ComponentEvent ) = {}
    def componentHidden(e :ComponentEvent ) = {}

    def componentResized(e :ComponentEvent ) = {

      surface.setViewPortSize( getWidth, getHeight )
    }
  })

  if(useCrosshairCursor) {
    setCursor( THIN_CROSSHAIR_CURSOR )
  }

  override def paintComponent(g: Graphics): Unit = {
    surface.render(g.asInstanceOf[Graphics2D])
  }

  def createThinCrosshairCursor() : Cursor = {

    val size = 15
    val mid = 7
    val image = new BufferedImage( size, size, BufferedImage.TYPE_INT_ARGB )
    val g = image.getGraphics

    jeps// White half transparent outline (for visibility on black bg.)
    g.setColor( new Color( 255, 255, 255, 128 ) )
    g.drawLine( mid+1, 0, mid+1, size)
    g.drawLine( mid-1, 0, mid-1, size)
    g.drawLine( 0, mid+1, size, mid+1)
    g.drawLine( 0, mid-1, size, mid-1)

    // Black mid
    g.setColor( Color.BLACK )
    g.drawLine( mid, 0, mid, size)
    g.drawLine( 0, mid, size, mid)

    val toolkit = java.awt.Toolkit.getDefaultToolkit();
    val cursor = toolkit.createCustomCursor(image, new Point( mid, mid), "ThinCrosshair")

    cursor
  }

}