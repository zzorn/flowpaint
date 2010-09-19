package org.flowpaint.ui


import java.awt.event.{MouseEvent, MouseAdapter, MouseWheelEvent}


import org.flowpaint.brush.{Brush}
import java.awt.{Graphics2D, BasicStroke, Polygon, Color}
import javax.swing.JComponent
import org.flowpaint.property.Data
import org.flowpaint.util.{MathUtils, DataSample}


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

  private val darkColor: Color = new java.awt.Color( 0.25f, 0.25f, 0.25f )
  private val mediumColor: Color = new java.awt.Color( 0.75f, 0.75f, 0.75f)
  private val lightColor: Color = new java.awt.Color( 1f,1f,1f )

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

      def drawTriangles(color1: java.awt.Color, color2: java.awt.Color, x1: Float, y1: Float, x2: Float, y2: Float, d1 : Float, d2 :Float, size :Float ) {
/*
        triangle( color1, x1, y1, d1, d2, size )
*/
        triangle( color2, x2, y2, -d1, -d2, size )
      }

      val w = preview.getWidth()
      val h = preview.getHeight()
      val size = (Math.min(w, h) / 3).toInt
      val r = relativePosition
      val dx = if (isVertical) 0f else 1f
      val dy = if (isVertical) 1f else 0f
      val x1 = if (isVertical) 0f else r * w
      val x2 = if (isVertical) w - 1f else r * w
      val y1 = if (isVertical) r * h else 0f
      val y2 = if (isVertical) r * h else h - 1f
      
      g2.setColor(darkColor)
      g2.drawRect( 2,2,w-5,h-5 )

      drawTriangles( darkColor, darkColor, x1, y1, x2, y2, dx, dy, size+2 )
      drawTriangles( lightColor, lightColor,  x1, y1, x2, y2, dx, dy, size )
      drawTriangles( mediumColor, mediumColor, x1, y1, x2, y2, dx, dy, size-2 )

      g2.setColor(mediumColor)
      g2.drawRect( 1,1,w-3,h-3 )
      g2.setColor(mediumColor)
      g2.drawRect( 0,0,w-1,h-1 )
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

    relativePosition = MathUtils.clampToZeroToOne(relativePosition)

    updateBrush()
  }

  private val mouseUpdateListener = new MouseAdapter() {
    override def mousePressed(e: MouseEvent) {updatePosition(e)}

    override def mouseReleased(e: MouseEvent) {updatePosition(e)}

    override def mouseDragged(e: MouseEvent) {updatePosition(e)}

    override def mouseWheelMoved(e: MouseWheelEvent) {
      val amount = e.getWheelRotation()

      relativePosition = MathUtils.clampToZeroToOne(relativePosition + WHEEL_STEP * amount)

      updateBrush()
    }
  }


  private def updateBrush() {
    editedData.setFloatProperty(editedParameter, MathUtils.interpolate(relativePosition, startValue, endValue))
  }




}










