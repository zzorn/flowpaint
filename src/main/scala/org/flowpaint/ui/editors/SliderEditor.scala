package org.flowpaint.ui.editors


import _root_.org.flowpaint.property.Data
import java.awt._
import java.awt.event._

import java.awt.image.BufferedImage
import javax.swing.{JPanel, JComponent}

import util.GraphicsUtils._

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


  def description = axis.description

  def isVertical: Boolean = orientation == VerticalSlider

  def initializeAxis() {
    axis.initialize(null)
  }

  /**
   *  Paint the indicator showing the current position
   */
  protected def paintIndicator(g2: Graphics2D, width : Int, height: Int): Unit = {

    def edgeTriangle( color: java.awt.Color, x: Float, y: Float, d1 : Float, d2 :Float, size :Float ) {
      triangle( g2, color,
        x - d1 * size, y - d2 * size,
        x + d2 * size, y + d1 * size,
        x + d1 * size, y + d2 * size)
    }

    def drawTriangles(color1: java.awt.Color, color2: java.awt.Color, x1: Float, y1: Float, x2: Float, y2: Float, d1 : Float, d2 :Float, size :Float ) {
/* Looks better with only one
        edgeTriangle( color1, x1, y1, d1, d2, size )
*/
      edgeTriangle( color2, x2, y2, -d1, -d2, size )
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

    drawTriangles( darkColor, darkColor, x1, y1, x2, y2, dx, dy, size+2 )
    drawTriangles( lightColor, lightColor,  x1, y1, x2, y2, dx, dy, size )
    drawTriangles( mediumColor, mediumColor, x1, y1, x2, y2, dx, dy, size-2 )

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

  def updateAxisFromEditedData() = {
    axis.updateRelativePosition
  }



}







