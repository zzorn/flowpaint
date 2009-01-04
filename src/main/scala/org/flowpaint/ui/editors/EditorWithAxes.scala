package org.flowpaint.ui.editors




import java.awt.event._
import java.awt.{Graphics2D, Dimension, Graphics}
import javax.swing.{JPanel, JComponent}
import java.awt.image.BufferedImage
import util.{GraphicsUtils, StringUtils, MathUtils}
/**
 * Provides Axis utility class.
 *
 * @author Hans Haggstrom
 */

abstract class EditorWithAxes extends Editor {
  class BackgroundCachingImagePanel(backgroundPainter: (Graphics2D, Int, Int) => Unit,
                                   foregroundPainter: (Graphics2D, Int, Int) => Unit) extends JPanel {
    addComponentListener(new ComponentListener {
      def componentMoved(e: ComponentEvent) = {}

      def componentShown(e: ComponentEvent) = {}

      def componentHidden(e: ComponentEvent) = {}

      def componentResized(e: ComponentEvent) = {
        repaintBackground()
      }
    })

    repaintBackground()

    private var backgroundBuffer: BufferedImage = null

    def repaintBackground() {
      val w: Int = getWidth
      val h: Int = getHeight

      if ( w > 0 && h > 0) {
        backgroundBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        backgroundPainter(GraphicsUtils.toG2(backgroundBuffer.getGraphics), w, h)
      }
    }

    override def paintComponent(g: Graphics) {
      val g2: Graphics2D = g.asInstanceOf[Graphics2D]

      val w = getWidth()
      val h = getHeight()

      if (w > 0 && h > 0 ) {
        if (backgroundBuffer == null) repaintBackground

        g2.drawImage(backgroundBuffer, 0, 0, null)

        foregroundPainter(g2, w, h)
      }
    }
  }



  class Axis {
    var startValue = 0f
    var endValue = 1f
    var parameter: String = null
    var relativePosition = 0f
    var description: String = null

    def initialize(prefix: String) {

      def id(propertyName: String) = StringUtils.addPrefix(prefix, propertyName)

      parameter = getStringProperty(id("editedParameter"), null)
      startValue = getFloatProperty(id("startValue"), 0f)
      endValue = getFloatProperty(id("endValue"), 1f)
      description = getStringProperty(id("description"), "")

      updateRelativePosition()
    }

    def updateEditedData() {
      val pos = MathUtils.clampToZeroToOne(relativePosition)
      val value = MathUtils.interpolate(pos, startValue, endValue)
      editedData.setFloatProperty(parameter, value)
    }

    def updateRelativePosition() {
      val value = editedData.getFloatProperty(parameter, 0.5f * (startValue + endValue))
      relativePosition = if (startValue == endValue) startValue
      else (value - startValue) / (endValue - startValue)
    }
  }


  protected def updateBrush()
  protected def updateRelativePosition( relativeX : Float, relativeY : Float )
  protected def updateAxisFromMouseWheelEvent( rotation : Int )
  protected def initializeAxis()
  protected def description : String
  protected def paintBackground( g2 : Graphics2D, width : Int, height : Int)
  protected def paintIndicator( g2 : Graphics2D, width : Int, height : Int)
  protected def minSize : Int

  private var view : BackgroundCachingImagePanel = null

  protected final def createUi(): JComponent = {

    initializeAxis()

    view = new BackgroundCachingImagePanel( paintBackground, paintIndicator )
    view.setPreferredSize(new Dimension(minSize, minSize))
    view.setToolTipText(description)
    view.addMouseListener(mouseUpdateListener)
    view.addMouseMotionListener(mouseUpdateListener)
    view.addMouseWheelListener(mouseUpdateListener)

    return view
  }

  private val mouseUpdateListener = new MouseAdapter() {
    override def mousePressed(e: MouseEvent) {updatePosition(e)}

    override def mouseReleased(e: MouseEvent) {updatePosition(e)}

    override def mouseDragged(e: MouseEvent) {updatePosition(e)}

    override def mouseWheelMoved(e: MouseWheelEvent) {
      val amount = e.getWheelRotation()
      updateAxisFromMouseWheelEvent(amount)
      updateBrush()
    }
  }

  private def updatePosition(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    val rx = (1.0f * x) / (1.0f * view.getWidth())
    val ry = (1.0f * y) / (1.0f * view.getHeight())

    updateRelativePosition(rx, ry)

    updateBrush()
  }



}