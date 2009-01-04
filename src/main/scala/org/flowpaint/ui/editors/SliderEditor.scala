package org.flowpaint.ui.editors


import _root_.org.flowpaint.property.Data
import java.awt._
import javax.swing.{JPanel, JComponent}
import java.awt.event.{MouseEvent, MouseAdapter, MouseWheelEvent}


abstract sealed class SliderOrientation
case object VerticalSlider extends SliderOrientation()
case object HorizontalSlider extends SliderOrientation()

/**
 * A slider for editing a BrushProperty, with some undefined background (specified by implementing classes).
 *
 * @author Hans Haggstrom
 */
abstract class SliderEditor extends Editor {

  var startValue = 0f
  var endValue = 1f
  var orientation: SliderOrientation = HorizontalSlider
  var editedParameter : String = null

  var relativePosition = 0f

  private var background :JComponent = null

  private val STROKE_1 = new BasicStroke(1)
  private val WHEEL_STEP = 0.01f
  private val MIN_SIZE = 32

  private val darkColor: Color = new java.awt.Color( 0.25f, 0.25f, 0.25f )
  private val mediumColor: Color = new java.awt.Color( 0.75f, 0.75f, 0.75f)
  private val lightColor: Color = new java.awt.Color( 1f,1f,1f )

  private def calculateRelativePosition() : Float = {
    val value = editedData.getFloatProperty(editedParameter, 0.5f * (startValue + endValue))
    if (startValue == endValue) startValue
    else (value - startValue) / (endValue - startValue)
  }


  /**
   * Renders a preview background of some sort for the slider component.
   */
  protected def paintBackground( g2 : Graphics2D, width : Int, height: Int )


  protected def createUi(): JComponent = {

    // Get editor settings
    editedParameter = getStringProperty( "editedParameter", null )
    startValue = getFloatProperty( "startValue", 0f )
    endValue = getFloatProperty( "endValue", 1f )
    relativePosition = calculateRelativePosition()

    // Create the UI component
    background = new JPanel() {
      override def paintComponent(g: Graphics) {
        val g2: Graphics2D = g.asInstanceOf[Graphics2D]

        val w = getWidth()
        val h = getHeight()

        paintBackground(g2, w, h)
        paintIndicator( g2, w, h )
      }
    }
    background.setPreferredSize(new Dimension(MIN_SIZE, MIN_SIZE))
    background.setToolTipText(getStringProperty( "description", null ))
    background.addMouseListener(mouseUpdateListener)
    background.addMouseMotionListener(mouseUpdateListener)
    background.addMouseWheelListener(mouseUpdateListener)

    return background
  }

  def isVertical: Boolean = orientation == VerticalSlider

  /**
   *  Paint the indicator showing the current position
   */
  private def paintIndicator(g2: Graphics2D, width : Int, height: Int): Unit = {


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
/* Looks better with only one
        triangle( color1, x1, y1, d1, d2, size )
*/
      triangle( color2, x2, y2, -d1, -d2, size )
    }

    val w = width
    val h = height
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


  private def updatePosition(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    if (isVertical)
      relativePosition = (1.0f * y) / (1.0f * background.getHeight())
    else
      relativePosition = (1.0f * x) / (1.0f * background.getWidth())

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







