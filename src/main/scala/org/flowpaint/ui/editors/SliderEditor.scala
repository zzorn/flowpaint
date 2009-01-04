package org.flowpaint.ui.editors


import _root_.org.flowpaint.property.Data
import java.awt._
import java.awt.event._

import java.awt.image.BufferedImage
import javax.swing.{JPanel, JComponent}
abstract sealed class SliderOrientation
case object VerticalSlider extends SliderOrientation()
case object HorizontalSlider extends SliderOrientation()

/**
 * A slider for editing a BrushProperty, with some undefined background (specified by implementing classes).
 *
 * @author Hans Haggstrom
 */
abstract class SliderEditor extends EditorWithAxes {

  val axis = new Axis()

  val orientation : SliderOrientation= HorizontalSlider

  private val STROKE_1 = new BasicStroke(1)
  private val WHEEL_STEP = 0.01f
  protected val minSize = 32

  private val darkColor: Color = new java.awt.Color( 0.25f, 0.25f, 0.25f )
  private val mediumColor: Color = new java.awt.Color( 0.75f, 0.75f, 0.75f)
  private val lightColor: Color = new java.awt.Color( 1f,1f,1f )



  def description = axis.description

  def isVertical: Boolean = orientation == VerticalSlider

  def initializeAxis() {
    axis.initialize(null)
  }

  /**
   *  Paint the indicator showing the current position
   */
  protected def paintIndicator(g2: Graphics2D, width : Int, height: Int): Unit = {


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
    val r = axis.relativePosition
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



  protected def updateRelativePosition(relativeX: Float, relativeY: Float) {

    if (isVertical) axis.relativePosition = relativeY
    else axis.relativePosition = relativeX

    axis.relativePosition = util.MathUtils.clampToZeroToOne(axis.relativePosition)
  }

  protected def updateAxisFromMouseWheelEvent(rotation: Int) {
    axis.relativePosition = util.MathUtils.clampToZeroToOne(axis.relativePosition + WHEEL_STEP * rotation)
  }


  protected def updateBrush() {
    axis.updateEditedData()
  }




}







