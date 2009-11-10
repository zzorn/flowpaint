package org.flowpaint.util


import java.awt.event._
import javax.swing.JComponent


/**
 * Mixin that provides a bit more sane mouse event listening.
 *
 * @author Hans Haggstrom
 */
trait MouseEventMixin extends JComponent {

  addMouseWheelListener( new MouseAdapter() {
    override def mouseWheelMoved(e: MouseWheelEvent) = {
      val rotation = e.getWheelRotation
      mouseWheelRolled( rotation, e )
    }
  })

  addMouseListener( new MouseAdapter() {
    override def mouseClicked(e: MouseEvent) = {
      if (LeftMouse usedInEvent e) leftClick( e.getX, e.getY, e )
      if (RightMouse usedInEvent e) rightClick( e.getX, e.getY, e )
      if (MiddleMouse usedInEvent e) middleClick( e.getX, e.getY, e )
    }
  })

  def leftClick( x : Int, y : Int, e: MouseEvent ) {}
  def rightClick( x : Int, y : Int, e: MouseEvent ) {}
  def middleClick( x : Int, y : Int, e: MouseEvent ) {}
  def mouseWheelRolled( roll : Int, e: MouseEvent ) {}

  def addDragListener[T <: AnyRef]( button : MouseButton, dragListener : DragListener[T] ) {
    val handler = new DragHandler[T]( button, dragListener )

    addMouseListener( handler )
    addMouseMotionListener( handler )
  }



}


