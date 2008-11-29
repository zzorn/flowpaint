package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import brush.{Brush, BrushProperty}
import java.awt.event.{MouseEvent, MouseWheelEvent, MouseAdapter, MouseListener}

import java.awt.{Graphics2D, BorderLayout, BasicStroke, Graphics}
import javax.swing.JComponent
import renderer.{SingleRenderSurface, RenderSurface}


/**
 * A slider with a brush stroke as a background.
 *
 * @author Hans Haggstrom
 */
class BrushSliderUi(editedData: DataSample,
                   p: BrushProperty,
                   previewBrush: => Brush,
                   changeListener: () => Unit) extends SliderUi(editedData, p, changeListener) {



  protected def createBackground(indicatorPainter: (Graphics2D) => Unit): JComponent = {
    new BrushPreview(previewBrush, brushPreviewStrokeGenerator, indicatorPainter)
  }

  private def brushPreviewStrokeGenerator(f: Float, w: Float, h: Float, dataSample: DataSample) {

    if (orientation == VerticalSlider) {
      dataSample.setProperty("x", w / 2f)
      dataSample.setProperty("y", h * f)
    }
    else {
      dataSample.setProperty("x", w * f)
      dataSample.setProperty("y", h / 2f)
    }

    dataSample.setProperty("pressure", 0.5f)
    dataSample.setProperty("time", f * 0.5f)
    dataSample.setProperty(editedParameter, util.MathUtils.interpolate(f, startValue, endValue))

  }


}













