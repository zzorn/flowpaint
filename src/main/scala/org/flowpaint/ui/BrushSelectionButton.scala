package org.flowpaint.ui

import org.flowpaint.brush.Brush
import java.awt.Color
import java.awt.event.{ComponentListener, MouseAdapter}

import javax.swing.JComponent
import org.flowpaint.util.DataSample
import org.flowpaint.FlowPaintController

/**
 * A button showing a preview of a brush, which can be clicked to select the brush in the flowpaint controller.
 * 
 * @author Hans Haggstrom
 */
class BrushSelectionButton( brush : Brush ) {

  val ui : JComponent = createUi()

  private var mousePressedOnThisButton = false
  private val UNPRESSED_COLOR = new Color(230,230,230)
  private val PRESSED_COLOR=  new Color(250,210,100)

  ui.setBackground( UNPRESSED_COLOR )
  ui.repaint()

  def createUi() : JComponent = {

    val preview = new BrushPreview( brush )

    preview.setToolTipText( brush.name )

    def press() {
      mousePressedOnThisButton = true
      preview.setBackgroundColor( PRESSED_COLOR )
      preview.update()
    }

    def unpress() {
      mousePressedOnThisButton = false
      preview.setBackgroundColor( UNPRESSED_COLOR )
      preview.update()
    }

    preview.addMouseListener(new MouseAdapter() {
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

    unpress()

    return preview
  }





}