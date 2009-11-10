package org.flowpaint.util

import java.awt.event.{MouseAdapter, MouseEvent}

/**
 *
 *
 * @author Hans Haggstrom
 */
class DragHandler[T <: AnyRef]( dragButton : MouseButton, listener : DragListener[T] ) extends MouseAdapter {
  sealed class DragState
  object NoDrag extends DragState
  object DragButtonPressed extends DragState
  object DragOngoing extends DragState

  var startX : Int = 0
  var startY : Int = 0
  var prevX : Int = 0
  var prevY : Int = 0
  var dragState : DragState = NoDrag
  var draggedObject : T = null.asInstanceOf[T]

  var startEvent : MouseEvent = null

  private def dragButtonPressed( e: MouseEvent ) {
    dragState = DragButtonPressed
    startX = e.getX
    startY = e.getY
    prevX = startX
    prevY = startY
    startEvent = e
  }

  private def endDrag(e: MouseEvent) {
    listener.dragEnd( startX, startY, e.getX, e.getY, e, draggedObject )
    resetDrag()
  }

  private def startDrag(e: MouseEvent) {
    dragState = DragOngoing
    draggedObject = listener.dragStart( startX, startY, startEvent )
    startEvent = null
  }

  private def dragUpdate(e: MouseEvent) {
    val x = e.getX
    val y = e.getY
    listener.drag( startX, startY, prevX, prevY, x, y, e, draggedObject )
    prevX = x
    prevY = y
  }

  private def resetDrag() {
    dragState = NoDrag
    startEvent = null
    draggedObject = null.asInstanceOf[T]
    startX = 0
    startY = 0
    prevX = 0
    prevY = 0
  }

  private def cancelDrag(e: MouseEvent) {
    listener.dragCanceled( startX, startY, e.getX, e.getY, e, draggedObject )
    resetDrag()
  }

  override def mousePressed(e: MouseEvent) {
    val dragButtonUsed = dragButton usedInEvent e
    dragState match {
      case NoDrag => if ( dragButtonUsed ) dragButtonPressed( e )
      case DragButtonPressed => if( !dragButtonUsed ) resetDrag
      case DragOngoing =>if (!dragButtonUsed) cancelDrag( e )
    }
  }

  override def mouseReleased(e: MouseEvent) {
    if( dragButton usedInEvent e) dragState match {
      case NoDrag =>
      case DragButtonPressed => resetDrag
      case DragOngoing => dragUpdate( e ) ; endDrag( e )
    }
  }

  override def mouseDragged(e: MouseEvent) {
    if (dragButton pressedInEvent  e) dragState match {
      case NoDrag =>
      case DragButtonPressed => startDrag( e ) ; dragUpdate( e )
      case DragOngoing => dragUpdate( e )
    }
  }

}

