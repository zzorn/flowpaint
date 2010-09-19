package org.flowpaint.ui.editors

import java.awt.{Graphics2D, Color}
import org.flowpaint.util.GraphicsUtils._
import org.flowpaint.util.{ColorUtils, PropertyRegister, MathUtils}
/**
 * A 2D slider editor visualizing the editor background with saturation - luminance gradients.
 *
 * @author Hans Haggstrom
 */

class SaturationLightnessEditor extends Slider2DEditor {

  propertiesThatShouldCauseBackgroundRedraw = List( "hue" )

  def calculateLight( rx : Float, ry : Float ) = (1f-ry + rx) / 2f

  def calculateSat( x : Float, y : Float ) = {
    if (x <= 0 && y <= 0) 1f
    else if (x >= 1 && y >= 1) 0f
    else if (x <= 0 && y >= 1) 0f
    else if (x >= 1 && y <= 0) 0f
    else if ( x > y) 1f - y / (y + (1f - x))
    else 1f - x / (x + (1f - y))
  }

  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) = {

    val hue = MathUtils.clampToZeroToOne( editedData.getFloatProperty( PropertyRegister.HUE, 0 ) )

    fillRectFunction(g2, 0,0,width, height, (rx :Float, ry : Float) => {

      // Saturation
      //val sat = 1f - ry
      val sat = calculateSat( rx, ry )

      // Lightness
      val lightness = calculateLight( rx,ry )

      val (r,g,b) = ColorUtils.HSLtoRGB( hue, sat, lightness )

      new Color(r, g, b)
    })

  }

  override protected def updateRelativePosition(relativeX: Float, relativeY: Float) {

    val x = MathUtils.clampToZeroToOne( relativeX )
    val y = MathUtils.clampToZeroToOne( relativeY )

    verticalAxis.relativePosition = calculateSat( x, y )
    horizontalAxis.relativePosition = calculateLight( x, y )
  }


  override protected def calculateIndicatorRelativePosition() : (Float, Float) = {

    val sat = verticalAxis.relativePosition
    val light = horizontalAxis.relativePosition

    val nearnessToCenter = 1f - Math.abs(2f * light - 1f).toFloat
    val s = (0.5f - sat) * nearnessToCenter

    val x = s + light
    val y = s + 1f - light
    ( x, y )
  }


}