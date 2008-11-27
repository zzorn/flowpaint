package org.flowpaint.ui

import _root_.org.flowpaint.brush.Brush
import _root_.org.flowpaint.util.DataSample
import java.awt.{Graphics2D, BorderLayout, BasicStroke, Graphics}
import renderer.SingleRenderSurface
/**
 * 
 *
 * @author Hans Haggstrom
 */

class SliderUi( editedData : DataSample,
                editedParameter : String,
                startValue : Float,
                endValue : Float,
                previewBrush : => Brush,
                changeListener : => Unit ) extends ParameterUi(editedData) {

  // TODO: Slider implementation, with a brush used to render the preview

  // TODO: Reuse BrushPreview


  var relativePosition = 0.5f
  var orientation : Orientation = Vertical

  private val STROKE_1 = new BasicStroke(1)
  private val preview = new BrushPreview( previewBrush, brushPreviewStrokeGenerator )

  add( preview, BorderLayout.CENTER )


  abstract sealed class Orientation
  case object Vertical extends Orientation ()
  case object Horizontal extends Orientation ()


  def brushPreviewStrokeGenerator(f:Float, w:Float, h:Float, dataSample:DataSample) {

    if ( orientation == Vertical) {
      dataSample.setProperty("x", w / 2f)
      dataSample.setProperty("y", h * f)
    }
    else  {
      dataSample.setProperty("x", w * f)
      dataSample.setProperty("y", h / 2f)
    }

    dataSample.setProperty("pressure", 0.5f)
    dataSample.setProperty("time", f * 0.5f)
    dataSample.setProperty(editedParameter, util.MathUtils.interpolate( f, startValue, endValue ))

  }





}