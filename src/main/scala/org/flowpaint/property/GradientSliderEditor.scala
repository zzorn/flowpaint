package org.flowpaint.property

import org.flowpaint.ink.{PixelProcessorMetadata, Ink}
import org.flowpaint.util.ListenableList
import org.flowpaint.brush.Brush
import javax.swing.JComponent
import org.flowpaint.ui.slider.InkSliderUi

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