package org.flowpaint.model
import brush.Brush
import property.Data

/**
 * A path is a sequence of samples, forming a path on a 2D surface.  The Path can be connected to other paths.
 * A path can be iterated in both directions, and it can be tested for overlap with a rectangular area.
 * 
 * @author Hans Haggstrom
 */
class Path(brush : Brush) {

  val commonProperties = new Data

  private var startPoint : PathPoint = null
  private var endPoint : PathPoint = null

  def start = startPoint
  def end = endPoint

  def addPoint( p : PathPoint ) {

    p.previous = endPoint
    p.next = null
    p.path = this

    if (endPoint != null) endPoint.next = p

    endPoint = p

    if (startPoint == null) startPoint = p
  }



}

