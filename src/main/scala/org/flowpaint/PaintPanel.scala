package org.flowpaint

import java.awt.event.{ComponentListener, ComponentEvent}
import java.awt.{Graphics2D, Graphics}
import javax.swing.JPanel
import renderer.RenderSurface
import java.awt.Cursor

/**
 * A swing component that can be painted on.
 *
 * @author Hans Haggstrom
 */
class PaintPanel( surface: RenderSurface, useCrosshairCursor : Boolean ) extends JPanel {

  addComponentListener( new ComponentListener{
    def componentMoved(e :ComponentEvent ) = {}
    def componentShown(e :ComponentEvent ) = {}
    def componentHidden(e :ComponentEvent ) = {}

    def componentResized(e :ComponentEvent ) = {

      surface.setViewPortSize( getWidth, getHeight )
    }
  })

  if(useCrosshairCursor) {
    // TODO: Thinner crosshair needed.
    setCursor( new Cursor( Cursor.CROSSHAIR_CURSOR ) )
  }

  override def paintComponent(g: Graphics): Unit = {
    surface.render(g.asInstanceOf[Graphics2D])
  }
  
}