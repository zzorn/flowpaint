package org.flowpaint.ui

import brush.Brush
import java.awt.Color
import java.awt.event.{ComponentListener, MouseAdapter}

import javax.swing.JComponent

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionButton( brush : Brush ) {

  val ui : JComponent = createUi()


  def createUi() : JComponent = {

    val preview = new BrushPreview( brush, null )

    val componentListener : ComponentListener= new ComponentListener() {
      def componentMoved(e: java.awt.event.ComponentEvent) {}

      def componentShown(e: java.awt.event.ComponentEvent) {}

      def componentHidden(e: java.awt.event.ComponentEvent) {}

      def componentResized(e: java.awt.event.ComponentEvent) { /*updateStroke()*/ }
    }

    preview.addComponentListener(componentListener)

    return preview

  }

/*
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

*/

}