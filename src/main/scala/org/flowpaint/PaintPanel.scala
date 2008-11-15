package org.flowpaint

import java.awt.event.{ComponentListener, ComponentEvent}
import java.awt.{Graphics2D, Graphics}
import javax.swing.JPanel
import renderer.RenderSurface

/**
 * A swing component that can be painted on.
 *
 * @author Hans Haggstrom
 */
class PaintPanel( surface: RenderSurface) extends JPanel {

  addComponentListener( new ComponentListener{
    def componentMoved(e :ComponentEvent ) = {}
    def componentShown(e :ComponentEvent ) = {}
    def componentHidden(e :ComponentEvent ) = {}

    def componentResized(e :ComponentEvent ) = {

      println ("View resize: "+e)

      surface.setViewPortSize( getWidth, getHeight )
    }
  })

  override def paintComponent(g: Graphics): Unit = {
    surface.render(g.asInstanceOf[Graphics2D])
  }
  
}