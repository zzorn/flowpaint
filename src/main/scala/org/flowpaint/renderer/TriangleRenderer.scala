package org.flowpaint.renderer

import util.DataSample

/**
 *       Renders a triangle
 *
 * @author Hans Haggstrom
 */
class TriangleRenderer {
  type PixelCallback = (Int, Int, DataSample) => Unit

  val RED_COLOR = new DataSample( ("red", 1f), ("alpha", 1f) )

  def renderTriangle(viewWidth: Int, viewHeight: Int,
                    xi0: Float, yi0: Float, xi1: Float, yi1: Float, xi2: Float, yi2: Float,
                    pixelCallback: PixelCallback, data: DataSample) {


    /**
     *   Renders a part of a scanline
     */
    def fillLine(scanline: Int, x_ : Int, endX_ : Int) {

      // Clip top and bottom and off screen
      if (scanline >= 0 && scanline < viewHeight && x_ < viewWidth && endX_ >= 0 ) {

        // Clip left side
        var x = if (x_ < 0) 0 else x_

        // Clip right side
        val endX = if (endX_ > viewWidth) viewWidth else endX_

        // Render scanline
        while (x < endX) {
          pixelCallback(x, scanline, data)
          x += 1
        }
      }
    }

    def rasterizeTrapetzoid(startY: Int, endY: Int, aX: Int, aY: Int, aD: Float, bX: Int, bY: Int, bD: Float) {

      var y = 0f
      var startX: Int = 0
      var endX: Int = 0
      var scanline: Int = startY

      while (scanline < endY) {
        y = scanline.toFloat

        // OPTIMIZE: We could get rid of a multiplication if we add the delta instead.
        // But could eat a bit of precision maybe?
        startX = (aX + ((y - aY) * aD)).toInt
        endX = (bX + ((y - bY) * bD)).toInt

        if (startX < endX) fillLine( scanline, startX, endX );
        else fillLine( scanline, endX, startX );

        scanline += 1
      }
    }


    // Round to ints, or we get rendering artefacts
    var x0 = xi0.toInt
    var x1 = xi1.toInt
    var x2 = xi2.toInt

    var y0 = yi0.toInt
    var y1 = yi1.toInt
    var y2 = yi2.toInt

    // Sort points according to y coordinate so that y0 <= y1 <= y2
    var dt : Int= 0
    if (y1 < y0) {dt = y0; y0 = y1; y1 = dt;  dt = x0; x0 = x1; x1 = dt} // Swap point 0 and 1
    if (y2 < y1) {dt = y1; y1 = y2; y2 = dt;  dt = x1; x1 = x2; x2 = dt} // Swap point 1 and 2
    if (y1 < y0) {dt = y0; y0 = y1; y1 = dt;  dt = x0; x0 = x1; x1 = dt} // Swap point 0 and 1 again

    // Calculate slope coefficients
    def calculateCoefficient(dx: Int, dy: Int): Float = if (dy == 0) 0f else (dx.toFloat) / (dy.toFloat)
    val d01 : Float = calculateCoefficient(x0 - x1, y0 - y1)
    val d12 : Float = calculateCoefficient(x1 - x2, y1 - y2)
    val d02 : Float = calculateCoefficient(x0 - x2, y0 - y2)

/*
    def checkGradient( d : Float, y : Int, x:Int, targetY:Int, expectedX:Int) {
      val calculatedX: Float = d * (targetY - y) + x
      if ( calculatedX == expectedX ) println ("gradient ok") else println ("Gradient fail: calculatedX = " + calculatedX + ", expected x: " + expectedX)
    }
    checkGradient(d01, y0, x0, y1, x1)
    checkGradient(d02, y0, x0, y2, x2)
    checkGradient(d12, y1, x1, y2, x2)
*/


    // Render the upper and lower part of the triangle (above and below the middle y point)

    rasterizeTrapetzoid(y0, y1, x0, y0, d02, x0, y0, d01)
    rasterizeTrapetzoid(y1, y2, x0, y0, d02, x1, y1, d12)


/*
    pixelCallback( xi0.toInt, yi0.toInt, RED_COLOR  )
    pixelCallback( xi1.toInt, yi1.toInt, RED_COLOR  )
    pixelCallback( xi2.toInt, yi2.toInt, RED_COLOR  )
*/
  }


}
