package org.flowpaint.ui.editors
import _root_.scala.Math
import java.awt.{Graphics2D, Color}
import util.GraphicsUtils._
import util.{ColorUtils, PropertyRegister, MathUtils}


/**
 * A 2D slider editor for picking a hue.
 *
 * @author Hans Haggstrom
 */
class HueEditor extends SliderEditor {

  val NUM_BANDS = 6

  override val minSize = NUM_BANDS * 24

  def calculateHue( x : Float, y : Float ) : Float = {

    val huePerBand = 1f / NUM_BANDS.toFloat

    val clampedY = Math.max( 0.001, Math.min(y, 0.999) ).toFloat
    val clampedX = Math.max( 0.001, Math.min(x, 0.999) ).toFloat

    val band = Math.floor(clampedY * NUM_BANDS).toFloat
    val offset = clampedX / NUM_BANDS
    var hue = band * huePerBand  + offset

    if (hue >= 1f) hue = 0f
    if (hue < 0f) hue = 0f

    hue
  }

  def calculateCoordinatesForHue( hue : Float ) : (Float, Float) = {

    val offset = (hue % (1f / NUM_BANDS.toFloat)) * NUM_BANDS.toFloat
    val band = Math.floor(hue * NUM_BANDS.toFloat ).toFloat

    val rx = offset
    val ry = (band + 0.5f) / NUM_BANDS.toFloat

    return (rx, ry)
  }

  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) = {

    fillRectFunction(g2, 0,0,width, height, (rx :Float, ry : Float) => {

      val hue = calculateHue(rx, ry)

      // Add some borders between the hue gradients for visual separation
      val border = 0.35f
      val bandPos = (ry % (1f / NUM_BANDS.toFloat)) * (NUM_BANDS.toFloat)
      val bandDistance =  MathUtils.clampToZeroToOne( if (bandPos < 0.5f) bandPos else 1f - bandPos )

//      val sat = if (bandDistance < border || bandDistance > 1f - border) 0f else 1f
      val v = 1f - ( if ( bandDistance < border ) bandDistance / border else 1f )
      val sat = 1f - v * v * v * v * v
/*
      val sat = if (bandDistance < border) 0f else 1f
*/

      val (r,g,b) = ColorUtils.HSLtoRGB( hue, sat, 0.5f )

      new Color( r, g, b)
    })

  }


  override protected def updateRelativePosition(relativeX: Float, relativeY: Float) {

    axis.relativePosition = calculateHue( relativeX, relativeY )

  }


  override protected def paintIndicator(g2: Graphics2D, width: Int, height: Int) {

    val w = width - borderSize*2
    val h = height - borderSize*2

    val (rx, ry) = calculateCoordinatesForHue(axis.relativePosition)
    val x = borderSize +  w * rx
    val y = borderSize + h * ry 

    val size = 7

    drawDiamondIndicator( g2, blackColor, darkColor, mediumColor, lightColor, size, x, y )

  }
}