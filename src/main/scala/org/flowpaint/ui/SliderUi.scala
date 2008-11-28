package org.flowpaint.ui

import _root_.org.flowpaint.brush.Brush
import _root_.org.flowpaint.util.DataSample
import java.awt.event.{MouseEvent, MouseWheelEvent, MouseAdapter, MouseListener}

import java.awt.{Graphics2D, BorderLayout, BasicStroke, Graphics}
import renderer.SingleRenderSurface
/**
 * 
 *
 * @author Hans Haggstrom
 */
// TODO: Refactor to have JComponent as member, not inherit from it
class SliderUi( editedData : DataSample,
                editedParameter : String,
                startValue : Float,
                endValue : Float,
                previewBrush : => Brush,
                changeListener : () => Unit ) extends ParameterUi(editedData) {


  var relativePosition = {
    val value = editedData.getProperty( editedParameter, 0.5f*(startValue+endValue) )
    if (startValue == endValue)  startValue
    else (value - startValue) / (endValue - startValue)
  }

  var orientation : Orientation = Vertical

  private val STROKE_1 = new BasicStroke(1)
  private val preview = new BrushPreview( previewBrush, brushPreviewStrokeGenerator, paintIndicator )
  private val WHEEL_STEP = 0.01f

  add( preview, BorderLayout.CENTER )

  private def  isVertical: Boolean = orientation == Vertical

  private def updatePosition( e : MouseEvent  ) {
    val x = e.getX
    val y = e.getY

    if (isVertical)
      relativePosition = (1.0f * y) / (1.0f*getHeight())
    else
      relativePosition = (1.0f * x) / (1.0f*getWidth())

    relativePosition = util.MathUtils.clampToZeroToOne( relativePosition  )
    
    updateBrush()
  }

  private val mouseUpdateListener = new MouseAdapter() {
    override def mousePressed(e : MouseEvent ) { updatePosition(e) }
    override def mouseReleased(e : MouseEvent ) {updatePosition(e) }
    override def mouseDragged(e : MouseEvent ){updatePosition(e) }

    override def mouseWheelMoved(e : MouseWheelEvent ){
      val amount = e.getWheelRotation()

      relativePosition = util.MathUtils.clampToZeroToOne(  relativePosition + WHEEL_STEP * amount   )

      updateBrush()
    }
  }

  preview.addMouseListener( mouseUpdateListener )
  preview.addMouseMotionListener( mouseUpdateListener  )
  preview.addMouseWheelListener( mouseUpdateListener  )


  private def updateBrush() {
    editedData.setProperty( editedParameter, util.MathUtils.interpolate( relativePosition, startValue, endValue ) )
    changeListener()
  }

  /**
   * Paint the indicator showing the current position
   */
  private def paintIndicator(g2: Graphics2D): Unit = {

    def line( color : java.awt.Color, x1:Float, y1:Float, x2:Float, y2:Float ) {
      g2.setStroke(STROKE_1)
      g2.setColor( color )
      g2.drawLine( x1.toInt, y1.toInt, x2.toInt, y2.toInt )

    }

    val w = preview.getWidth().toFloat
    val h = preview.getHeight().toFloat
    val r = relativePosition
    val dx = if (isVertical) 0f else 1f
    val dy = if (isVertical) 1f else 0f
    val x1 = if (isVertical) 0f else r * w
    val x2 = if (isVertical) w else r * w
    val y1 = if (isVertical) r * h else 0f
    val y2 = if (isVertical) r * h else h

    line( java.awt.Color.BLACK, x1-dx, y1-dy, x2-dx, y2-dy )
    line( java.awt.Color.WHITE, x1, y1, x2, y2 )
    line( java.awt.Color.BLACK, x1+dx, y1+dy, x2+dx, y2+dy )
  }




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