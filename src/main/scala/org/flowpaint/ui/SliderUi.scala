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
                previewBrush : => Brush ) extends ParameterUi(editedData) {

  // TODO: Slider implementation, with a brush used to render the preview

  // TODO: Reuse BrushPreview

  private val STROKE_1 = new BasicStroke(1)


  abstract sealed class Orientation
  case object Vertical extends Orientation ()
  case object Horizontal extends Orientation ()

  var relativePosition = 0.5f

  var orientation : Orientation = Vertical

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



  val preview = new BrushPreview(previewBrush, brushPreviewStrokeGenerator ) {

    override def paintComponents(p1: Graphics): Unit = {
      super.paintComponent(p1)

      val g2 :Graphics2D = p1.asInstanceOf[Graphics2D]

      val r = relativePosition
      val w = getWidth().toFloat
      val h = getHeight().toFloat

      val vertical: Boolean = orientation == Vertical
      val x1 = if (vertical)  0f else r * w
      val x2 = if (vertical)  w else r *w
      val y1 = if (vertical)  h * r else 0f
      val y2 = if (vertical)  h * r else h

      g2.setColor(java.awt.Color.BLACK)
      g2.setStroke( STROKE_1)
      g2.drawLine( x1.toInt, y1.toInt, x2.toInt, y2.toInt  )
    }

  }

  add( preview, BorderLayout.CENTER )

}