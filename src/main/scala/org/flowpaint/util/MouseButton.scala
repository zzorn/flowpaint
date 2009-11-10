package org.flowpaint.util


import java.awt.event.{MouseEvent, InputEvent}

/**
 *
 *
 * @author Hans Haggstrom
 */
abstract case class MouseButton( mouseButton : Int, buttonMask : Int ) {
  def usedInEvent( e : MouseEvent ) = e.getButton == mouseButton
  def pressedInEvent( e : MouseEvent ) = (e.getModifiersEx & buttonMask) == buttonMask

}

case object LeftMouse   extends MouseButton(MouseEvent.BUTTON1, InputEvent.BUTTON1_DOWN_MASK)
case object MiddleMouse extends MouseButton(MouseEvent.BUTTON2, InputEvent.BUTTON2_DOWN_MASK)
case object RightMouse  extends MouseButton(MouseEvent.BUTTON3, InputEvent.BUTTON3_DOWN_MASK)


