package org.flowpaint.ui.editors
import java.awt.{Graphics2D, Color}
import util.{MathUtils}
import util.GraphicsUtils._
/**
 * A simple slider editor, showing as image a simple triangle that grows to the right.
 *
 * @author Hans Haggstrom
 */
class VolumeSliderEditor extends SliderEditor {

  protected def paintBackground(g2: Graphics2D, width: Int, height: Int) {

    val r = MathUtils.clampToZeroToOne( getFloatProperty( "red", 0 ) )
    val g = MathUtils.clampToZeroToOne( getFloatProperty( "green", 0 ) )
    val b = MathUtils.clampToZeroToOne( getFloatProperty( "blue", 0 ) )

    g2.setColor( Color.WHITE )
    g2.fillRect( 0, 0, width, height )


    antialiased(g2) {

      triangle( g2,  new Color( r, g, b ), 0, height / 2, width, 0, width, height )

    }
  }




}