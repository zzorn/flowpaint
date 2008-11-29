package org.flowpaint.property

import _root_.org.flowpaint.ink.Ink
import javax.swing.JComponent

/**
 * 
 *
 * @author Hans Haggstrom
 */
class GradientSliderEditor(title:String,
                               propertyName : String,
                               min:Float,
                               max:Float,
                               inks : List[Ink]) extends DataEditor(title) {

  def createEditor(editedData: Data): JComponent = {
    // TODO: Use the InkSlider...
    return null
  }

}