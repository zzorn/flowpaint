package org.flowpaint.util

import java.awt.event.MouseEvent

/**
 *
 *
 * @author Hans Haggstrom
 */
trait DragListener[T <: AnyRef] {
  def dragStart( x : Int, y : Int, e : MouseEvent ) : T = null.asInstanceOf[T]
  def drag( startX :Int, startY :Int, prevX : Int, prevY : Int, x : Int, y : Int, e : MouseEvent, draggedObject : T ) {}
  def dragEnd( startX :Int, startY :Int, endX : Int, endY : Int, e : MouseEvent, draggedObject : T ) {}
  def dragCanceled( startX :Int, startY :Int, endX : Int, endY : Int, e : MouseEvent, draggedObject : T ) {}
}


