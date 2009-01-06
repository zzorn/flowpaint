package org.flowpaint.renderer


import _root_.org.flowpaint.property.Data
import java.awt._
import java.awt.image.{MemoryImageSource, DirectColorModel, BufferedImage}
import util.{DataSample, BoundingBox, PropertyRegister}
/**
 *          A RenderSurface implementation that uses a single surface to render on, the same size as the screen
 *
 * @author Hans Haggstrom
 */
class SingleRenderSurface(override val pictureProvider: PictureProvider) extends RenderSurface {
  private var width = 0
  private var height = 0

  def getWidth = width
  def getHeight = height

  private val TRANSPARENT_COLOR = new Color(0, 0, 0, 0).getRGB()

  private var imageSource : MemoryImageSource = null
  private var imageData : Array[Int] = null
  private var image : Image = null
  private var initialized = false

  private val updatedArea = new BoundingBox()

  def isInitialized = initialized

  def clear() {
    clearToColor(java.awt.Color.WHITE)
  }

  def clearToColor(color: Color) {
    if (color == null) throw new IllegalArgumentException( "color should not be null" )

    if (initialized) {
      val c = color.getRGB
      var i = 0
      while ( i < imageData.length ) { imageData(i) = c; i += 1 }

      updatedArea.includeArea( 0, 0, width, height )
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

        initialize( width, height )

        updateSurface()
      }
  }

  private def initialize(w : Int, h : Int) {

    // Don't include alpha, as it takes longer to render due to masking
    val rgbColorModel : DirectColorModel= new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff);

    imageData = new Array[Int]( w * h )
    imageSource = new MemoryImageSource( w, h, rgbColorModel, imageData, 0, w )
    imageSource.setAnimated(true)

    image = Toolkit.getDefaultToolkit().createImage( imageSource )

    initialized = true
  }

  def getImage = image
  
  def createBufferedImage : BufferedImage = {
    if (!initialized) return null

    val buf = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB )
    renderFullArea( buf.getGraphics )

    buf
  }

  def renderFullArea(context: Graphics) {

    if (initialized ) {

      updateImage

      context.drawImage(image, 0, 0, null)
    }

  }

  def renderChangedArea(context: Graphics) {

    if (initialized && !updatedArea.isEmpty) {

      val x1 = updatedArea.getMinX
      val y1 = updatedArea.getMinY
      val x2 = updatedArea.getMaxX + 1
      val y2 = updatedArea.getMaxY + 1

      updateImage

      context.drawImage(image, x1, y1, x2, y2, x1, y1, x2, y2, null)
    }

  }


  private def updateImage {
    if (! updatedArea.isEmpty) {

      imageSource.newPixels( updatedArea.getMinX, updatedArea.getMinY,
                             updatedArea.getWidth, updatedArea.getHeight )

      updatedArea.clear
    }
  }



  def putPixel(x: Int, y: Int, sample: Data) {

    if (initialized &&
            x >= 0 && y >= 0 &&
            x < width && y < height) {

      val alpha: Float = sample.getFloatProperty(PropertyRegister.ALPHA, 0)

      if (alpha > 0) {

        val imageDataIndex = x + y * width

        var color = util.ColorUtils.createRGBAColor(
          sample.getFloatProperty(PropertyRegister.RED, 0),
          sample.getFloatProperty(PropertyRegister.GREEN, 0),
          sample.getFloatProperty(PropertyRegister.BLUE, 0),
          alpha)

        if (alpha < 1) {
          val originalColor =  imageData( imageDataIndex )
          color = util.ColorUtils.mixRGBWithAlpha(color, originalColor)
        }

        imageData(imageDataIndex) = color

        updatedArea.includePoint( x, y )
      }
    }
  }

  def putPixel(x: Int, y: Int, colorCode : Int) {

    if (initialized &&
            x >= 0 && y >= 0 &&
            x < width && y < height) {

      imageData(x + y * width) = colorCode
      updatedArea.includePoint( x, y )
    }
  }

  def provideContent(minX: Float, minY: Float,
                    maxX: Float, maxY: Float,
                    colorCalculator: (Int, Int) => Int) {


    if (initialized)
      {
        // Use segment bounding box to reudce the area needed to be iterated through
        val sX = Math.max(minX.toInt, 0)
        val sY = Math.max(minY.toInt, 0)
        val eX = Math.min(maxX.toInt, width - 1)
        val eY = Math.min(maxY.toInt, height - 1)

        // NOTE: While should be faster than a for comprehension.
        var y = sY
        while (y <= eY) {

          var x = sX
          while (x <= eX) {

            var color: Int = colorCalculator(x, y)

            val alpha = util.ColorUtils.getAlpha(color)
            if (alpha > 0) {

              val imageDataIndex = x + y * width

              if (alpha < 1) {
                val originalColor = imageData(imageDataIndex)
                color = util.ColorUtils.mixRGBWithAlpha(color, originalColor)
              }

              imageData(imageDataIndex) = color
            }

            x += 1
          }

          y += 1
        }

        updatedArea.includeArea( sX, sY, eX, eY  )
      }
  }


}