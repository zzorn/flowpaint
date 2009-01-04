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
  private val blackColor: Color = new java.awt.Color( 0,0,0)
  private val darkColor: Color = new java.awt.Color( 0.25f, 0.25f, 0.25f )
  private val mediumColor: Color = new java.awt.Color( 0.75f, 0.75f, 0.75f)
  private val lightColor: Color = new java.awt.Color( 1f,1f,1f )


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


    def line(color: java.awt.Color, x1: Float, y1: Float, x2: Float, y2: Float) {
      g2.setStroke(STROKE_1)
      g2.setColor(color)
      g2.drawLine(x1.toInt, y1.toInt, x2.toInt, y2.toInt)

    }

    def drawRhomb( color : Color, r : Float, x : Float, y : Float ) {
      triangle( g2, color, x-r, y, x, y-r, x+r,y )
      triangle( g2, color, x+r, y, x, y+r, x-r,y )
    }

    val w = width
    val h = height
    val size = 7
    val x = w * horizontalAxis.relativePosition
    val y = h * verticalAxis.relativePosition

    g2.setColor(darkColor)
    g2.drawRect( 2,2,w-5,h-5 )

    drawRhomb( darkColor, size+2, x, y )
    drawRhomb( mediumColor, size, x, y )
    drawRhomb( darkColor,  size, x, y+2 )
    drawRhomb( lightColor, size, x, y-2 )
    drawRhomb( mediumColor, size-2, x, y )

    g2.setColor(mediumColor)
    g2.drawRect( 1,1,w-3,h-3 )
    g2.setColor(mediumColor)
    g2.drawRect( 0,0,w-1,h-1 )
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




}










