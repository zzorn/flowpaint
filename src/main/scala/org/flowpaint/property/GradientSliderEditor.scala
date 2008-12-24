package org.flowpaint.property

import _root_.org.flowpaint.ink.{PixelProcessorMetadata, Ink}
import util.ListenableList
import brush.Brush
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
                               pixelPrcessorMetadatas: ListenableList[PixelProcessorMetadata])
        extends DataEditor(title) {

  def createEditor(editedData: Data, brush : Brush): JComponent = {

    val inkslider = new InkSliderUi( editedData, title, property, min, max, pixelPrcessorMetadatas  )

    inkslider.getUi()
  }

}