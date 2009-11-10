package org.flowpaint.renderer.packedsurface


import org.flowpaint.util.ParameterChecker._
import org.flowpaint.util.Rectangle

/**
 * Float raster of some size.
 * 
 * @author Hans Haggstrom
 */
class Raster( x : Int, y : Int, w : Int, h : Int ) extends Rectangle {

  reposition( x, y )
  resize( w, h )

  private var myX1 : Int = _
  private var myY1 : Int = _
  private var myWidth : Int = _
  private var myHeight : Int = _

  def x1 = myX1
  def y1 = myY1
  def width = myWidth
  def height = myHeight

  private var data : Array[Float] = _

  def putPixel( x : Int, y : Int, value : Float ) {
    data( index( x, y ) ) = value
  }

  def getPixel(x : Int, y : Int) : Float = {
    data( index( x, y ) )
  }

  def reposition( x : Int, y : Int) {
    myX1 = x
    myY1 = y
  }

  def resize( w : Int, h : Int ) {
    requirePositiveInteger( w, 'w )
    requirePositiveInteger( h, 'h )

    myWidth = w
    myHeight = h

    data = new Array[Float]( width * height )
  }

  private def index( x : Int, y : Int ) : Int = {
    requireInRange( x, 'x, x1, x2 )
    requireInRange( y, 'y, y1, y2 )
    x + y * width
  }


}

