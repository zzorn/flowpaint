package org.flowpaint.ui.editors
import java.awt.{Graphics2D, Color}

/**
 * A simple slider editor for editing a size, showing as image a simple triangle that grows to the right.
 *
 * @author Hans Haggstrom
 */
class SizeSliderEditor extends SliderEditor {

  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) {

    g2.setColor( Color.WHITE )
    g2.fillRect( 0, 0, width, height )

    triangle( g2,  Color.BLACK, 0, height / 2, width, 0, width, height )
  }


  def triangle( g2: Graphics2D, color: java.awt.Color, x0: Float, y0: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
    val xs = new Array[Int]( 3 )
    val ys = new Array[Int]( 3 )

    xs(0) = x0.toInt
    xs(1) = x1.toInt
    xs(2) = x2.toInt

    ys(0) = y0.toInt
    ys(1) = y1.toInt
    ys(2) = y2.toInt

    g2.setColor(color)
    g2.fillPolygon( xs, ys, 3 )
  }


}