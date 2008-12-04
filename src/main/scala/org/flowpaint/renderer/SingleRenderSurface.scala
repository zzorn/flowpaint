package org.flowpaint.renderer


import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Graphics, Color}
import util.DataSample
import util.PropertyRegister

/**
 *          A RenderSurface implementation that uses a single surface to render on, the same size as the screen
 *
 * @author Hans Haggstrom
 */
class SingleRenderSurface(override val pictureProvider: PictureProvider) extends RenderSurface {
  var width = 0
  var height = 0
  var buffer: BufferedImage = null
  private val TRANSPARENT_COLOR = new Color(0, 0, 0, 0).getRGB()


  def clear() {
    clearToColor(java.awt.Color.WHITE)
  }

  def clearToColor(color: Color) {
    if (buffer != null) {
      val graphics = buffer.getGraphics
      graphics.setColor(color)
      graphics.fillRect(0, 0, width, height)
    }
  }

  def updateSurface() {
    pictureProvider.updateSurface(this)
  }

  def setViewPortSize(aWidth: Int, aHeight: Int) {
    if (aWidth < 0 || aHeight < 0) throw new java.lang.IllegalArgumentException("Negative size not allowed")

    if (width != aWidth ||
            height != aHeight)
      {
        width = aWidth
        height = aHeight

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        updateSurface()
      }
  }


  def render(context: Graphics2D) {

    if (buffer != null) {
      context.drawImage(buffer, 0, 0, java.awt.Color.WHITE, null)
    }

  }


  def putPixel(x: Int, y: Int, sample: DataSample) {

    if (buffer != null &&
            x >= 0 && y >= 0 &&
            x < width && y < height) {

      val alpha: Float = sample.getProperty(PropertyRegister.ALPHA, 0)

      if (alpha > 0) {

        var color = util.ColorUtils.createRGBAColor(
          sample.getProperty(PropertyRegister.RED, 0),
          sample.getProperty(PropertyRegister.GREEN, 0),
          sample.getProperty(PropertyRegister.BLUE, 0),
          alpha)

        if (alpha < 1) {
          val originalColor = buffer.getRGB(x, y)
          color = util.ColorUtils.mixRGBWithAlpha(color, originalColor)
        }

        buffer.setRGB(x, y, color)
      }
    }
  }

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

        // NOTE: While should be a bit faster than a for comprehension.
        var y = sY
        while (y <= eY) {

          var x = sX
          while (x <= eX) {

            var color: Int = colorCalculator(x, y)

            val alpha = util.ColorUtils.getAlpha(color)
            if (alpha > 0) {

              if (alpha < 1) {
                val originalColor = buffer.getRGB(x, y)
                color = util.ColorUtils.mixRGBWithAlpha(color, originalColor)
              }

              buffer.setRGB(x, y, color)
            }

            x += 1
          }

          y += 1
        }
      }
  }


}