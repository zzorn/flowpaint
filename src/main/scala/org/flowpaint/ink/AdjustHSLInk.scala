package org.flowpaint.ink
import util.{DataSample, PropertyRegister, MathUtils}

/**
 * An ink that adjusts the red, green, and blue components using the hueDelta, saturationDelta, and lightnessDelta values.
 * 
 * @author Hans Haggstrom
 */

class AdjustHSLInk extends Ink {
  def processPixel(pixelData: DataSample) = {

    // Get adjustments
    val hueD = pixelData.getProperty( PropertyRegister.HUE_DELTA, 0 )
    val saturationD = pixelData.getProperty( PropertyRegister.SATURATION_DELTA, 0 )
    val lightnessD = pixelData.getProperty( PropertyRegister.LIGHTNESS_DELTA, 0 )

    if ( hueD != 0 || saturationD != 0 || lightnessD != 0 ) {

      // Get current color
      val r = pixelData.getProperty(PropertyRegister.RED,0.5f)
      val g = pixelData.getProperty(PropertyRegister.GREEN,0.5f)
      val b = pixelData.getProperty(PropertyRegister.BLUE,0.5f)

      // Convert current color to hsl space
      var (h, s, l)= util.ColorUtils.RGBtoHSL( r, g, b )

      // Apply the delta
      h = MathUtils.wrapToZeroToOne( h + hueD )
      s = MathUtils.clampToZeroToOne( s + saturationD )
      l = MathUtils.clampToZeroToOne( l + lightnessD )

      // Move back to rgb space
      val (r2, g2, b2)= util.ColorUtils.HSLtoRGB( h, s, l)

      // Store adjusted color
      pixelData.setProperty( PropertyRegister.RED, r2 )
      pixelData.setProperty( PropertyRegister.GREEN, g2)
      pixelData.setProperty( PropertyRegister.BLUE, b2 )
    }

  }
}