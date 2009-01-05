package org.flowpaint.ui

import brush.{Brush}
import java.awt.event.{MouseEvent, MouseWheelEvent, MouseAdapter, MouseListener}

import java.awt.{Graphics2D, BorderLayout, BasicStroke, Graphics}
import javax.swing.JComponent
import property.Data
import renderer.{SingleRenderSurface, RenderSurface}
import util.{DataSample, PropertyRegister}


/**
 *  A slider with a brush stroke as a background.
 *
 * @author Hans Haggstrom
 */
class BrushSliderUi(editedData: Data,
                   description: String,
                   property: String,
                   min: Float,
                   max: Float,
                   previewBrush: => Brush )
        extends SliderUi(editedData, description, property, min, max) {
  var background: BrushPreview = null

  protected def createBackground(indicatorPainter: (Graphics2D) => Unit): JComponent = {
    background = new BrushPreview(previewBrush, brushPreviewStrokeGenerator, indicatorPainter)
    return background
  }


  protected def updateUi() {
    if (background != null) background.updateUI()
  }

  private def brushPreviewStrokeGenerator(f: Float, w: Float, h: Float, dataSample: Data) {

    if (orientation == VerticalSlider) {
      dataSample.setFloatProperty(PropertyRegister.PATH_X, w / 2f)
      dataSample.setFloatProperty(PropertyRegister.PATH_Y, h * f)
    }
    else {
      dataSample.setFloatProperty(PropertyRegister.PATH_X, w * f)
      dataSample.setFloatProperty(PropertyRegister.PATH_Y, h / 2f)
    }

    dataSample.setFloatProperty(PropertyRegister.PRESSURE, 0.5f)
    dataSample.setFloatProperty(PropertyRegister.TIME, f * 0.5f)
    dataSample.setFloatProperty(editedParameter, util.MathUtils.interpolate(f, startValue, endValue))

  }


}













