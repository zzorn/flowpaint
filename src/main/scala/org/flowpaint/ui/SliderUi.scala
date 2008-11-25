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

  var relativePosition = 0.5f

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


  val preview = new BrushPreview(previewBrush, brushPreviewStrokeGenerator ) {

    override def paintComponents(p1: Graphics): Unit = {
      super.paintComponent(p1)

      val r = relativePosition
      val w = getWidth().toFloat
      val h = getHeight().toFloat
      val x1 = if (orientation==Vertical)  0f else r * w
      val x2 = if (orientation==Vertical)  w else r *w
      val y1 = if (orientation==Vertical)  h * r else 0f
      val y2 = if (orientation==Vertical)  h * r else h

      p1.setColor(java.awt.Color.BLACK)
      p1.drawLine( x1.toInt, y1.toInt, x2.toInt, y2.toInt  )
    }

  }

  add( preview, BorderLayout.CENTER )

}