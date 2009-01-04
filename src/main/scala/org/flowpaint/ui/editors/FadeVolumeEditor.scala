package org.flowpaint.ui.editors

import java.awt.{Graphics2D, Color}
import util.GraphicsUtils._
import util.{ColorUtils, MathUtils}
/**
 * A 2D slider editor visualizing the editor with a volume triangle and a fade.
 * 
 * @author Hans Haggstrom
 */

class FadeVolumeEditor extends Slider2DEditor {

  var hueProperty : String = null
  var saturationProperty : String = null
  var lightnessProperty : String = null

  override def onEditorCreated() {

    def addRedrawProperty( referenceName : String ) = {
      val propertyName = getStringProperty( referenceName, null )
      if (propertyName != null)
        propertiesThatShouldCauseBackgroundRedraw = propertyName :: propertiesThatShouldCauseBackgroundRedraw
      propertyName
    }

    hueProperty = addRedrawProperty( "hueParameter" )
    saturationProperty = addRedrawProperty( "saturationParameter" )
    lightnessProperty = addRedrawProperty( "lightnessParameter" )
  }

  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) = {

    def getProp( propName : String, default : Float ) : Float = {
      if (propName  == null) default
      else editedData.getFloatProperty( propName, default )
    }

    val hue = getProp( hueProperty, 0 )
    val sat = getProp( saturationProperty, 0 )
    val lig = getProp( lightnessProperty, 0.5f )

    fillRectFunction(g2, 0,0,width, height, (rx :Float, ry : Float) => {

      // Triangle pattern
      val centerDist = Math.abs( 0.5f - ry )
      val triangle = MathUtils.clampToZeroToOne( (centerDist - rx * 0.5f) * 100f )

      // Darkness pattern
      val darkness = (1f - rx* 0.5f)

      // Checker pattern
      val cx = ((rx * width).toInt / 16) % 2 == 0
      val cy = ((ry * height).toInt / 16) % 2 == 0
      val c = cx != cy
      val checker = if ( c ) 0.666f else 0.888f

      val light = MathUtils.interpolate( 0.1f, lig, triangle )

      // Alpha fade
      val t = 1f - Math.cos( ry * Math.Pi / 2 ).toFloat
      val s = MathUtils.interpolate( t, sat, 0 )
      val l = MathUtils.interpolate( t, light, checker ) * darkness

      val (r,g,b) = ColorUtils.HSLtoRGB( hue, s, l )

      new Color(r, g, b)
    })

  }
}