package org.flowpaint.ui.editors

import _root_.org.flowpaint.property.Data
import java.awt._
import javax.swing.{JPanel, JComponent}
import java.awt.event.{MouseEvent, MouseAdapter, MouseWheelEvent}
import util.GraphicsUtils._
import util.{StringUtils, MathUtils}
/**
 * A slider for editing two BrushProperties (or one wrapped on many lines),
 * with some undefined background (specified by implementing classes).
 *
 * @author Hans Haggstrom
 */
// TODO: Some code is shared with SliderEditor, move to common superclass
abstract class Slider2DEditor extends EditorWithAxes {


  val horizontalAxis = new Axis
  val verticalAxis = new Axis

  private val WHEEL_STEP = 0.01f
  protected val minSize = 128

  private val STROKE_1 = new BasicStroke(1)


  /**
   * Renders a preview background of some sort for the 2d slider component.
   */
  protected def paintBackground( g2 : Graphics2D, width : Int, height: Int )


  protected def initializeAxis() {
    verticalAxis.initialize( "vertical" )
    horizontalAxis.initialize( "horizontal" )
  }

  protected def description = getStringProperty( "description", horizontalAxis.description + " x " + verticalAxis.description )


  /**
   *  Paint the indicator showing the current position
   */
  protected def paintIndicator(g2: Graphics2D, width : Int, height: Int): Unit = {

    val w = width - borderSize*2
    val h = height - borderSize*2
    val (rx, ry) = calculateIndicatorRelativePosition()
    val x = borderSize +  w * rx
    val y = borderSize + h * ry

    val size = 7

    drawDiamondIndicator( g2, blackColor, darkColor, mediumColor, lightColor, size, x, y )
  }

  protected def calculateIndicatorRelativePosition() : (Float, Float) = {
    (horizontalAxis.relativePosition, verticalAxis.relativePosition)
  }

  protected def updateAxisFromMouseWheelEvent(rotation: Int)  {
    horizontalAxis.relativePosition = util.MathUtils.clampToZeroToOne(horizontalAxis.relativePosition + WHEEL_STEP * rotation)
  }


  protected def updateRelativePosition(relativeX: Float, relativeY: Float)  {
    verticalAxis.relativePosition = MathUtils.clampToZeroToOne(relativeY)
    horizontalAxis.relativePosition = MathUtils.clampToZeroToOne(relativeX)
  }


  protected def updateBrush() {
    horizontalAxis.updateEditedData()
    verticalAxis.updateEditedData()
  }


  def updateAxisFromEditedData() = {
    verticalAxis.updateRelativePosition
    horizontalAxis.updateRelativePosition
  }
}










