package org.flowpaint.property

import _root_.org.flowpaint.brush.Brush
import javax.swing.JComponent
import ui.BrushSliderUi

/**
 * 
 *
 * @author Hans Haggstrom
 */

class BrushSliderEditor(title :String,
                       property : String,
                       min:Float,
                       max:Float,
                       previewBrush: Brush )
        extends DataEditor( title  ) {

  def createEditor(editedData: Data): JComponent = {

    new BrushSliderUi( editedData, title, property, min, max, previewBrush )

  }
  
}