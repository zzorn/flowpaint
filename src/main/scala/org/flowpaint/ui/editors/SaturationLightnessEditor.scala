package org.flowpaint.ui.editors
/**
 * 
 * 
 * @author Hans Haggstrom
 */


import java.awt.{Graphics2D, Color}
import util.GraphicsUtils._
import util.{ColorUtils, PropertyRegister, MathUtils}
/**
 * A 2D slider editor visualizing the editor background with saturation - luminance gradients.
 *
 * @author Hans Haggstrom
 */

class SaturationLightnessEditor extends Slider2DEditor {

  propertiesThatShouldCauseBackgroundRedraw = List( "hue" )

  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) = {

    val hue = MathUtils.clampToZeroToOne( editedData.getFloatProperty( PropertyRegister.HUE, 0 ) )

    fillRectFunction(g2, 0,0,width, height, (rx :Float, ry : Float) => {

      // Saturation
      val sat = 1f - ry

      // ightness
      val lightness = 1f - rx

      val (r,g,b) = ColorUtils.HSLtoRGB( hue, sat, lightness )

      new Color(r, g, b)
    })

  }

}