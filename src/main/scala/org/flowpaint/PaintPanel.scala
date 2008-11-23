package org.flowpaint

import java.awt._
import java.awt.event.{ComponentListener, ComponentEvent}
import java.awt.image.BufferedImage
import javax.swing.JPanel
import renderer.RenderSurface

/**
 *  A swing component that can be painted on.
 *
 * @author Hans Haggstrom
 */
class PaintPanel(surface: RenderSurface, useCrosshairCursor: Boolean) extends JPanel {
  val THIN_CROSSHAIR_CURSOR = createThinCrosshairCursor()

  addComponentListener(new ComponentListener {
    def componentMoved(e: ComponentEvent) = {}

    def componentShown(e: ComponentEvent) = {}

    def componentHidden(e: ComponentEvent) = {}

    def componentResized(e: ComponentEvent) = {

      surface.setViewPortSize(getWidth, getHeight)
    }
  })

  if (useCrosshairCursor) {
    setCursor(THIN_CROSSHAIR_CURSOR)
  }

  override def paintComponent(g: Graphics): Unit = {
    surface.render(g.asInstanceOf[Graphics2D])
  }

  def createThinCrosshairCursor(): Cursor = {

    val size = 18
    val mid = 9
    val cut = 3
    val image = new BufferedImage(size + 2, size + 2, BufferedImage.TYPE_INT_ARGB)
    val g = image.getGraphics

    g.setColor(Color.WHITE)
    g.drawLine(mid, 0, mid, mid - cut)
    g.drawLine(mid, mid + cut, mid, size)
    g.drawLine(0, mid, mid - cut, mid)
    g.drawLine(mid + cut, mid, size, mid)

    val offs = 1
    g.setColor(Color.BLACK)
    g.drawLine(mid + offs, offs, mid + offs, mid - cut + offs)
    g.drawLine(mid + offs, mid + cut + offs, mid + offs, size + offs)
    g.drawLine(offs, mid + offs, mid - cut + offs, mid + offs)
    g.drawLine(mid + cut + offs, mid + offs, size + offs, mid + offs)

    val toolkit = java.awt.Toolkit.getDefaultToolkit();
    val cursor = toolkit.createCustomCursor(image, new Point(mid+1, mid+1), "ThinCrosshair")

    cursor
  }

  def createCrosshairCursor(): Cursor = {

    val size = 18
    val mid = 9
    val image = new BufferedImage(size + 2, size + 2, BufferedImage.TYPE_INT_ARGB)
    val g = image.getGraphics

    g.setColor(Color.BLACK)
    g.drawLine(mid, 0, mid, size)
    g.drawLine(0, mid, size, mid)
    g.drawLine(mid - 1, 0, mid - 1, size)
    g.drawLine(0, mid - 1, size, mid - 1)
    g.drawLine(mid + 1, 0, mid + 1, size)
    g.drawLine(0, mid + 1, size, mid + 1)

    g.setColor(Color.WHITE)
    g.drawLine(mid, 1, mid, size - 1)
    g.drawLine(1, mid, size - 1, mid)

    val toolkit = java.awt.Toolkit.getDefaultToolkit();
    val cursor = toolkit.createCustomCursor(image, new Point(mid, mid), "ThickCrosshair")

    cursor
  }

}