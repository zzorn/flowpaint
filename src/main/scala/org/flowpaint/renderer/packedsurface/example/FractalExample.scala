package org.flowpaint.renderer.packedsurface.example


import util.SimpleFrame

/**
 * 
 * 
 * @author Hans Haggstrom
 */
object FractalExample {
  def main(args: Array[String]) {

    val epsilon = 0.0001f

    def mandelIter( x : Float, y : Float, steps : Int ) : Int = {

      val c1 = 0.762f
      val c2 = 0.1233f

      if ( (x.isInfinite || y.isInfinite ) || steps > 255 ) {
        steps
      }
      else {
        val x2 = x * x - y * y - c1
        val y2 = 2 * x * y + c2
        mandelIter( x2, y2, steps + 1 )
      }
    }


    def mandelbrot( x : Float, y : Float) : Float = {

      val value = mandelIter( x / 100f, y / 100f, 0 )

      ((value / 255f) min 1f) max 0f
    }

    new SimpleFrame( "Fractal Example", new VisibleSurface( mandelbrot ) )
  }
}

