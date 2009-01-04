package org.flowpaint.ui.editors
/**
 * 
 * 
 * @author Hans Haggstrom
 */


package org.flowpaint.ui.editors

import java.awt.{Graphics2D, Color}
import util.GraphicsUtils._
import util.MathUtils

/**
 * A 2D slider editor visualizing the editor background with saturation - brightness gradients.
 *
 * @author Hans Haggstrom
 */

class SaturationValueEditor extends Slider2DEditor {
  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) = {

    val r = MathUtils.clampToZeroToOne( getFloatProperty( "red", 0 ) )
    val g = MathUtils.clampToZeroToOne( getFloatProperty( "green", 0 ) )
    val b = MathUtils.clampToZeroToOne( getFloatProperty( "blue", 0 ) )

    g2.setColor( Color.WHITE )
    g2.fillRect( 0, 0, width, height )

    val border = 3
    fillRectFunction(g2, border,border,width-border*2, height-border*2, (rx :Float, ry : Float) => {

      // Triangle pattern
      val centerDist = Math.abs( 0.5f - ry )
      val triangle = MathUtils.clampToZeroToOne( (centerDist - rx * 0.5f) * 100f - 0.4f )

      // Checker pattern
      val bx = ((rx * width).toInt / 16) % 2 == 0
      val by = ((ry * height).toInt / 16) % 2 == 0
      val b = bx != by
      val checker = if ( b) 0.333f else 0.666f

      // Alpha fade
      val v = MathUtils.interpolate( ry, triangle, checker )

      new Color(v, v, v)
    })

  }
}