package org.flowpaint.ink
import _root_.org.flowpaint.property.Data
import org.flowpaint.util.{ColorUtils, DataSample, PropertyRegister, MathUtils}

/**
 * An ink that adjusts the red, green, and blue components using the hueDelta, saturationDelta, and lightnessDelta values.
 * 
 * @author Hans Haggstrom
 */

class AdjustHSLInk extends Ink {
  def processPixel(pixelData: Data) = {

    // Get adjustments
    val hueD = pixelData.getFloatProperty( PropertyRegister.HUE_DELTA, 0 )
    val saturationD = pixelData.getFloatProperty( PropertyRegister.SATURATION_DELTA, 0 )
    val lightnessD = pixelData.getFloatProperty( PropertyRegister.LIGHTNESS_DELTA, 0 )

    if ( hueD != 0 || saturationD != 0 || lightnessD != 0 ) {

      // Get current color
      val r = pixelData.getFloatProperty(PropertyRegister.RED,0.5f)
      val g = pixelData.getFloatProperty(PropertyRegister.GREEN,0.5f)
      val b = pixelData.getFloatProperty(PropertyRegister.BLUE,0.5f)

      // Convert current color to hsl space
      var (h, s, l)= ColorUtils.RGBtoHSL( r, g, b )

      // Apply the delta
      h = MathUtils.wrapToZeroToOne( h + hueD )
      s = MathUtils.clampToZeroToOne( s + saturationD )
      l = MathUtils.clampToZeroToOne( l + lightnessD )

      // Move back to rgb space
      val (r2, g2, b2)= ColorUtils.HSLtoRGB( h, s, l)

      // Store adjusted color
      pixelData.setFloatProperty( PropertyRegister.RED, r2 )
      pixelData.setFloatProperty( PropertyRegister.GREEN, g2)
      pixelData.setFloatProperty( PropertyRegister.BLUE, b2 )
    }

  }
}