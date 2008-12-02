package org.flowpaint.ui
import brush.Brush
import java.awt.{Dimension, Color}
import javax.swing.{JPanel, JLabel}
import net.miginfocom.swing.MigLayout
import property.DataEditor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BrushUi {

  def ui = editorPanel.ui
  val editorPanel = new ParameterPanel()

  private var brush : Brush = null

  private val PREVIEW_BACKGROUND_COLOR: Color = new Color(0.666f, 0.666f, 0.666f)

  def setBrush( brushToShow : Brush ) {

    if (brush != brushToShow) {
      brush = brushToShow

      editorPanel.clear()

      if (brush != null){


        // Add brush title
        editorPanel.addUi( new JLabel( brush.name ))

        // Add brush preview
        val preview: BrushPreview = new BrushPreview(brush)
        preview.setPreferredSize( new Dimension( 64,64 ) )
        preview.setBackgroundColor( PREVIEW_BACKGROUND_COLOR )
        editorPanel.addUi( preview)

        // Create the editors for editing this brush
        brush.getEditors().foreach( (d: DataEditor) => {
          editorPanel.addUi( d.createEditor( brush.settings )  ) } )

      }

      editorPanel.ui.repaint()
    }
  }
}