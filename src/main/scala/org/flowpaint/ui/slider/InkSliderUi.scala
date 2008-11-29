package org.flowpaint.ui.slider

import _root_.org.flowpaint.brush.{Brush, BrushProperty}
import _root_.org.flowpaint.ink.Ink
import _root_.org.flowpaint.util.DataSample
import javax.swing.JComponent
import java.awt.Graphics2D

/**
 * 
 *
 * @author Hans Haggstrom
 */

class InkSliderUi(editedData: DataSample,
                   p: BrushProperty,
                   inks : List[Ink],
                   changeListener: () => Unit)
        extends SliderUi( editedData,p,changeListener) {


  protected def createBackground(indicatorPainter: (Graphics2D) => Unit): JComponent = {

    //TODO
    return null

  }
}