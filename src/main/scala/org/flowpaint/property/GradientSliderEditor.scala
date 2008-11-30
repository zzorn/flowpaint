package org.flowpaint.property

import _root_.org.flowpaint.ink.Ink
import javax.swing.JComponent
import ui.slider.InkSliderUi

/**
 * 
 *
 * @author Hans Haggstrom
 */
class GradientSliderEditor(title:String,
                               property : String,
                               min:Float,
                               max:Float,
                               inks : List[Ink]) extends DataEditor(title) {

  def createEditor(editedData: Data): JComponent = {

    new InkSliderUi( editedData, title, property, min, max, previewBrush )

    // TODO: Use the InkSlider...
    return null
  }

}