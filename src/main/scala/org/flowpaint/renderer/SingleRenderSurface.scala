package org.flowpaint.renderer


import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Graphics, Color}

/**
 *         A RenderSurface implementation that uses a single surface to render on, the same size as the screen
 *
 * @author Hans Haggstrom
 */
class SingleRenderSurface(override val pictureProvider: PictureProvider) extends RenderSurface {
  var width = 0
  var height = 0
  var buffer: BufferedImage = null


  def clear() {
    if (buffer != null) {
      val graphics = buffer.getGraphics
      graphics.setColor(java.awt.Color.WHITE)
      graphics.fillRect(0, 0, width, height)
    }
  }

  def setViewPortSize(aWidth: Int, aHeight: Int) {
    if (aWidth < 0 || aHeight < 0) throw new java.lang.IllegalArgumentException("Negative size not allowed")

    if (width != aWidth ||
            height != aHeight)
      {
        width = aWidth
        height = aHeight

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        pictureProvider.updateSurface(this)
      }
  }


  def render(context: Graphics2D) {

    if (buffer != null) {
      context.drawImage(buffer, 0, 0, java.awt.Color.WHITE, null)
    }

  }


  private val TRANSPARENT_COLOR = new Color(0, 0, 0, 0).getRGB()

  def provideContent(minX: Float, minY: Float,
                    maxX: Float, maxY: Float,
                    colorCalculator: (Int, Int) => Int) {


    if (buffer != null)
      {
        // Use segment bounding box to reudce the area needed to be iterated through
        val sX = Math.max(minX.toInt, 0)
        val sY = Math.max(minY.toInt, 0)
        val eX = Math.min(maxX.toInt, width - 1)
        val eY = Math.min(maxY.toInt, height - 1)

        for (y <- sY to eY;
             x <- sX to eX)
          {
            val color: Int = colorCalculator(x, y)

            if (color != TRANSPARENT_COLOR)
              buffer.setRGB(x, y, color)
          }
      }
  }


}