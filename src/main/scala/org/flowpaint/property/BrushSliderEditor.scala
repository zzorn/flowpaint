package org.flowpaint.property

import org.flowpaint.brush.Brush
import javax.swing.JComponent
import org.flowpaint.ui.BrushSliderUi

/**
 * 
 *
 * @author Hans Haggstrom
 */

class BrushSliderEditor(title :String,
                       property : String,
                       min:Float,
                       max:Float )
        extends DataEditor( title  ) {

  def createEditor(editedData: Data, brush : Brush): JComponent = {

    val brushUi = new BrushSliderUi( editedData, title, property, min, max, brush )
    brushUi.getUi()
  }
  
}