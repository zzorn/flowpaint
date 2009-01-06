package org.flowpaint.renderer



/**
 * A scanline based quad renderer, that can interpolate values between corners.
 * 
 * @author Hans Haggstrom
 * @deprecated the TriangleRenderer is used instead at the moment.
 */
class QuadRenderer {

  case class Point( x : Float, y : Float )

  case class Edge( left : Point, right : Point )

  case class Corner( point : Point, previousEdge : Edge, nextEdge : Edge )


  trait Callback {
    def onPixel(x : Int, y : Int, positionAlongSegment : Float, positionAcrossSegment : Float )
  }

  def renderQuad( startEdge : Edge, endEdge : Edge, callback : Callback) {

    val leftEdge = Edge( startEdge.left, endEdge.left )
    val rightEdge = Edge( startEdge.right, endEdge.right )

    val c00 = Corner( startEdge.left, startEdge, leftEdge )
    val c10 = Corner( endEdge.left, leftEdge, endEdge )
    val c11 = Corner( endEdge.right, endEdge, rightEdge )
    val c01 = Corner( startEdge.right, rightEdge, startEdge )

    val corners = List (c00, c10, c11, c01)

    val topCorner : Corner = corners.reduceLeft( ( a : Corner, b : Corner ) => if (a.point.y < b.point.y) a else b )
    //var remainingCorners : List[Corner] = corners.remove( _ == topCorner )

    // Get the step amounts for the next and previous edges

    // Render the scanline

    // Increase Y, if we didn't reach a corner, render next line.
    // If we reached a corner, get the next edge along that side, until we have gone through all edges / corners / reached the bottommost y.

    null
  }

}