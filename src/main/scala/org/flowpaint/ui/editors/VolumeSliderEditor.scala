package org.flowpaint.ui.editors
import java.awt.{Graphics2D, BorderLayout, Color}
import javax.swing.{SwingConstants, JLabel}
import util.{MathUtils}
import util.GraphicsUtils._
/**
 * A simple slider editor, showing as image a simple triangle that grows to the right.
 *
 * @author Hans Haggstrom
 */
class VolumeSliderEditor extends SliderEditor {

  private val label = new JLabel()


    override def onEditorCreated() =  {
        super.onEditorCreated()

        view.setLayout(new BorderLayout())
        view.add( label, BorderLayout.CENTER )

        label.setText( getStringProperty( "title", "" ) )
        label.setForeground( Color.BLACK )
        label.setHorizontalAlignment( SwingConstants.CENTER )
    }

    protected def paintBackground(g2: Graphics2D, width: Int, height: Int) {

    val bg = 0.9f
    g2.setColor( new Color( bg, bg, bg ) )
    g2.fillRect( 0, 0, width, height )
        
    val r = MathUtils.clampToZeroToOne( getFloatProperty( "red", 0 ) )
    val g = MathUtils.clampToZeroToOne( getFloatProperty( "green", 0 ) )
    val b = MathUtils.clampToZeroToOne( getFloatProperty( "blue", 0 ) )
    val a = 0.25f


    antialiased(g2) {

      triangle( g2,  new Color( r, g, b, a ), 0, height / 2, width, 0, width, height )

    }

      
  }




}