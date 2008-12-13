package org.flowpaint.model

import _root_.org.flowpaint.renderer.{StrokeRenderer, RenderSurface}
import _root_.org.flowpaint.util.DataSample
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



  private def renderPath(surface: RenderSurface) {
    val segmentStartData: Data = new Data()
    val segmentEndData: Data = new Data()

    var segmentStart : PathPoint= startPoint
    var segmentEnd : PathPoint= if (startPoint != null)startPoint.next else null

    if (segmentStart != null) {

      segmentEndData.setValuesFrom( segmentStart.data )

      while( segmentEnd != null ) {
        // Remember the variable values along the line even if they are only present
        // in the points when they have changed from the previous value.
        segmentStartData.setValuesFrom( segmentStart.data )
        segmentEndData.setValuesFrom( segmentEnd.data )

        renderStrokeSegment(segmentStartData, segmentEndData, surface)

        segmentStart = segmentEnd
        segmentEnd = segmentEnd.next
      }
    }




  }

  private def renderStrokeSegment(startPoint: Data, endPoint: Data, surface: RenderSurface) {

    val renderer = new StrokeRenderer()
    renderer.drawStrokeSegment( startPoint, endPoint, brush, surface)

  }
}

