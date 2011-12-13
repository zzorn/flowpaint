package org.flowpaint.raster.image

import java.awt.image.{BufferedImage, DirectColorModel, MemoryImageSource}
import java.awt.{Color, Graphics, Toolkit, Image}
import org.flowpaint.util.{RectangleImpl, Rectangle, BoundingBox}

/**
 * Fast, low level access image container.
 */
final class FastImage(val width: Int, val height: Int) {
  require(width > 0, "Width should be positive")
  require(height > 0, "Height should be positive")

  private[this] var imageSource: MemoryImageSource = null
  private[this] var image: Image = null
  private[this] val updatedArea = new BoundingBox()
  private[this] var imageData: Array[Int] = null

  initialize()

  private def initialize() {

      // Don't include alpha for normal on screen rendering, as it takes longer due to masking
      //val rgbColorModel: DirectColorModel = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff, 0xff000000)
      val rgbColorModel: DirectColorModel = new DirectColorModel(24, 0xff0000, 0x00ff00, 0x0000ff)

      imageData = new Array[Int](width * height)
      imageSource = new MemoryImageSource(width, height, rgbColorModel, imageData, 0, width)
      imageSource.setAnimated(true)

      image = Toolkit.getDefaultToolkit.createImage(imageSource)

      clear()
  }

  def buffer: Array[Int] = imageData

  def clear() {
      clearToColor(Color.WHITE)
  }

  def clearToColor(color: Color) {
    require(color != null, "color should not be null")

    val c = color.getRGB
    var i = imageData.length - 1
    while (i >= 0) {
      imageData(i) = c
      i -= 1
    }

    updatedArea.includeArea(0, 0, width, height)
  }

  /*
  def renderRaster(raster: Raster, viewX: Int, viewY: Int) {
    rasterRenderer.render(raster, )

    val area = new RectangleImpl(viewX, viewY, width, height)
    raster.getBlocks(area) foreach { block =>
      block.copyToRgbImage(imageData, width, area)
    }

    updatedArea.includeArea(0, 0, width, height)
  }
*/
  def getImage: Image = image

  def renderToGraphics(context: Graphics) {
    context.drawImage(image, 0, 0, null)
  }

  def createBufferedImage: BufferedImage = {
    val buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

/*
        val transparentRgbColorModel: DirectColorModel = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff, 0xff000000)
        val transparentImageSource = new MemoryImageSource(width, height, transparentRgbColorModel, imageData, 0, width)
        val transparentImage = Toolkit.getDefaultToolkit().createImage(imageSource)
*/

//        buf.getGraphics.drawImage(transparentImage, 0, 0, null)

    buf.getGraphics.drawImage(image, 0, 0, null)

    buf
  }


}