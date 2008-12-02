package org.flowpaint.ui


import _root_.org.flowpaint.util.DataSample
import java.awt.event.{MouseEvent, MouseAdapter, MouseWheelEvent}


import brush.{Brush}
import java.awt.{Graphics2D, BasicStroke, Polygon, Color}
import javax.swing.JComponent
import property.Data


abstract sealed class SliderOrientation
case object VerticalSlider extends SliderOrientation()
case object HorizontalSlider extends SliderOrientation()


/**
 * A slider for editing a BrushProperty, with some undefined background (specified by implementing classes).
 *
 * @author Hans Haggstrom
 *  */
// TODO: Switch to editing Data instead, and change Brush to store it's default values in a Data.
abstract class SliderUi(editedData: Data,
                       description : String,
                       property : String,
                       min:Float,
                       max:Float ) extends ParameterUi(editedData ) {

  val editedParameter = property
  val startValue = min
  val endValue = max
  var orientation: SliderOrientation = HorizontalSlider

  private val STROKE_1 = new BasicStroke(1)
  private val WHEEL_STEP = 0.01f
  private val indicatorColor: Color = new java.awt.Color( 0.8f,0.8f,0.8f )

  var relativePosition = {
    val value = editedData.getFloatProperty(editedParameter, 0.5f * (startValue + endValue))
    if (startValue == endValue) startValue
    else (value - startValue) / (endValue - startValue)
  }


  editedData.addListener( (data:Data, changedProperty :String) => {
    if (preview != null) {
      updateUi()
    }
  })

  private var preview :JComponent = null

  /**
   * Create a preview background of some sort for the slider component.
   * Takes as a parameter a function to paint an overlay with the position indicator.
   */
  protected def createBackground(indicatorPainter : (Graphics2D) => Unit ) : JComponent

  protected def updateUi()

  protected def createUi(): JComponent = {

    /**
     *  Paint the indicator showing the current position
     */
    def paintIndicator(g2: Graphics2D): Unit = {


      def line(color: java.awt.Color, x1: Float, y1: Float, x2: Float, y2: Float) {
        g2.setStroke(STROKE_1)
        g2.setColor(color)
        g2.drawLine(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

      }

      def triangle( color: java.awt.Color, x: Float, y: Float, d1 : Float, d2 :Float, size :Float ) {
        val xs = new Array[Int]( 3 )
        val ys = new Array[Int]( 3 )

        xs(0) = (x - d1 * size).toInt
        xs(1) = (x + d2 * size).toInt
        xs(2) = (x + d1 * size).toInt

        ys(0) = (y - d2 * size).toInt
        ys(1) = (y + d1 * size).toInt
        ys(2) = (y + d2 * size).toInt

        g2.setColor(color)
        g2.fillPolygon( xs, ys, 3 )
      }

      val w = preview.getWidth().toFloat
      val h = preview.getHeight().toFloat
      val size = (Math.min(w, h) / 4).toInt
      val r = relativePosition
      val dx = if (isVertical) 0f else 1f
      val dy = if (isVertical) 1f else 0f
      val x1 = if (isVertical) 0f else r * w
      val x2 = if (isVertical) w else r * w
      val y1 = if (isVertical) r * h else 0f
      val y2 = if (isVertical) r * h else h

/*
      line(java.awt.Color.BLACK, x1 - dx, y1 - dy, x2 - dx, y2 - dy)
      line(java.awt.Color.WHITE, x1, y1, x2, y2)
      line(java.awt.Color.BLACK, x1 + dx, y1 + dy, x2 + dx, y2 + dy)
*/

      triangle( java.awt.Color.BLACK, x1, y1, dx, dy, size+1 )
      triangle( java.awt.Color.BLACK, x2, y2, -dx, -dy, size+1 )
      triangle( indicatorColor, x1, y1, dx, dy, size )
      triangle( indicatorColor, x2, y2, -dx, -dy, size )
    }

    preview = createBackground(paintIndicator)

    preview.setToolTipText(description)
    //preview.setPreferredSize()

    preview.addMouseListener(mouseUpdateListener)
    preview.addMouseMotionListener(mouseUpdateListener)
    preview.addMouseWheelListener(mouseUpdateListener)

    preview
  }

  def isVertical: Boolean = orientation == VerticalSlider

  private def updatePosition(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    if (isVertical)
      relativePosition = (1.0f * y) / (1.0f * preview.getHeight())
    else
      relativePosition = (1.0f * x) / (1.0f * preview.getWidth())

    relativePosition = util.MathUtils.clampToZeroToOne(relativePosition)

    updateBrush()
  }

  private val mouseUpdateListener = new MouseAdapter() {
    override def mousePressed(e: MouseEvent) {updatePosition(e)}

    override def mouseReleased(e: MouseEvent) {updatePosition(e)}

    override def mouseDragged(e: MouseEvent) {updatePosition(e)}

    override def mouseWheelMoved(e: MouseWheelEvent) {
      val amount = e.getWheelRotation()

      relativePosition = util.MathUtils.clampToZeroToOne(relativePosition + WHEEL_STEP * amount)

      updateBrush()
    }
  }


  private def updateBrush() {
    editedData.setFloatProperty(editedParameter, util.MathUtils.interpolate(relativePosition, startValue, endValue))
  }




}










