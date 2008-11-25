package org.flowpaint.ui

import _root_.org.flowpaint.brush.Brush
import _root_.org.flowpaint.util.DataSample
import java.awt.Graphics
import renderer.SingleRenderSurface
import java.awt.BorderLayout

/**
 * 
 *
 * @author Hans Haggstrom
 */

class SliderUi( editedData : DataSample,
                editedParameter : String,
                startValue : Float,
                endValue : Float,
                previewBrush : => Brush ) extends ParameterUi(editedData) {

  // TODO: Slider implementation, with a brush used to render the preview

  // TODO: Reuse BrushPreview

  abstract sealed class Orientation
  case object Vertical extends Orientation ()
  case object Horizontal extends Orientation ()


  var orientation : Orientation = Vertical

  def brushPreviewStrokeGenerator(f:Float, w:Float, h:Float, dataSample:DataSample) {

    if ( orientation == Vertical) {
      dataSample.setProperty("x", w / 2)
      dataSample.setProperty("y", h * f)
    }
    else  {
      dataSample.setProperty("x", w * f)
      dataSample.setProperty("y", h /2)
    }

    val pressure = 0.5f + 0.5f*Math.cos( 2*Math.Pi * f + Math.Pi ).toFloat
    dataSample.setProperty("pressure", 0.5f)

    dataSample.setProperty("time", f * 0.5f)
  }

  val preview = new BrushPreview(previewBrush, brushPreviewStrokeGenerator )

  add( preview, BorderLayout.CENTER )

}