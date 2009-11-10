package org.flowpaint.renderer


import _root_.org.flowpaint.property.Data
import _root_.scala.compat.Platform
import java.awt.image.{MemoryImageSource, DirectColorModel, BufferedImage}
import java.awt.{Toolkit, Graphics, Image, Color}
import pixelprocessor.ScanlineCalculator
import util.{DataSample, BoundingBox, PropertyRegister, MathUtils}

/**
 *          A RenderSurface implementation that uses a single surface to render on, the same size as the screen
 *
 * @author Hans Haggstrom
 */
class SingleRenderSurface(override val pictureProvider: PictureProvider, undoQueueSize : Int) extends RenderSurface {
    private var width = 0
    private var height = 0

    def getWidth = width

    def getHeight = height



    private var undoQueue : List[Array[Int]] = Nil
    private var redoQueue : List[Array[Int]] = Nil

    private val currentImageDataIndex = 0

    private val BACKGROUND = new Color(0.5f,0.5f,0.5f,1f)
    private val TRANSPARENT = new Color(0.5f, 0.5f, 0.5f, 0f)
    private val TRANSPARENT_COLOR = TRANSPARENT.getRGB()


    private var imageSource: MemoryImageSource = null
    private var image: Image = null
    private var initialized = false

    private val updatedArea = new BoundingBox()

    private var imageData: Array[Int] = null

    def isInitialized = initialized

    def clear() {
        clearToColor(TRANSPARENT)
    }

    def clearToColor(color: Color) {
        if (color == null) throw new IllegalArgumentException("color should not be null")

        if (initialized) {
            val c = color.getRGB
            var i = 0
            while (i < imageData.length) {imageData(i) = c; i += 1}

            updatedArea.includeArea(0, 0, width, height)
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

                initialize(width, height)

                updateSurface()
            }
    }

    private def initialize(w: Int, h: Int) {

        // Don't include alpha for normal on screen rendering, as it takes longer due to masking

      
        val rgbColorModel: DirectColorModel = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff, 0xff000000)

        undoQueue = Nil
        redoQueue = Nil

        imageData = new Array[Int](w * h)
        imageSource = new MemoryImageSource(w, h, rgbColorModel, imageData, 0, w)
        imageSource.setAnimated(true)

        image = Toolkit.getDefaultToolkit().createImage(imageSource)


        clear()

        initialized = true
    }

    def getImage = image

    def createBufferedImage: BufferedImage = {
        if (!initialized) return null

        val buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

/*
        val transparentRgbColorModel: DirectColorModel = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff, 0xff000000)
        val transparentImageSource = new MemoryImageSource(width, height, transparentRgbColorModel, imageData, 0, width)
        val transparentImage = Toolkit.getDefaultToolkit().createImage(imageSource)
*/


//        buf.getGraphics.drawImage(transparentImage, 0, 0, null)

        updateImage
        buf.getGraphics.drawImage(image, 0, 0, null)

        buf
    }

    def renderFullArea(context: Graphics) {

        if (initialized) {

            updateImage

            context.setColor( BACKGROUND )
            context.fillRect( 0,0,getWidth, getHeight )

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

            context.setColor( Color.WHITE )
            context.fillRect( x1, y1 , updatedArea.getWidth, updatedArea.getHeight  )

            context.drawImage(image, x1, y1, x2, y2, x1, y1, x2, y2, null)
        }

    }


    private def updateImage {
        if (!updatedArea.isEmpty) {

            imageSource.newPixels(updatedArea.getMinX, updatedArea.getMinY,
                updatedArea.getWidth, updatedArea.getHeight)

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
                    MathUtils.clampToZeroToOne(sample.getFloatProperty(PropertyRegister.RED, 0)),
                    MathUtils.clampToZeroToOne(sample.getFloatProperty(PropertyRegister.GREEN, 0)),
                    MathUtils.clampToZeroToOne(sample.getFloatProperty(PropertyRegister.BLUE, 0)),
                    MathUtils.clampToZeroToOne(alpha))

                if (alpha < 1) {
                    val originalColor = imageData(imageDataIndex)
                    color = util.ColorUtils.mixRGBWithAlpha(color, originalColor)
                }

                imageData(imageDataIndex) = color

                updatedArea.includePoint(x, y)
            }
        }
    }

    def putPixel(x: Int, y: Int, colorCode: Int) {

        if (initialized &&
                x >= 0 && y >= 0 &&
                x < width && y < height) {

            imageData(x + y * width) = colorCode
            updatedArea.includePoint(x, y)
        }
    }



  def getPixel(x: Int, y: Int) : Int = {
    if (initialized &&
        x >= 0 && y >= 0 &&
        x < width && y < height) {
        imageData(x + y * width)
    }
    else -1
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

                updatedArea.includeArea(sX, sY, eX, eY)
            }
    }

    def renderScanline(
                      scanline: Int,
                      x_ : Int,
                      endX_ : Int,
                      startSample_ : DataSample,
                      endSample_ : DataSample,
                      scanlineCalculator: ScanlineCalculator) {

        if (initialized)
            {

                // Clip top and bottom and off screen
                if (scanline >= 0 && scanline < height && x_ < width && endX_ >= 0 && endX_ > x_) {

                    // Clip left side
                    var startSample = startSample_
                    var x = if (x_ < 0) {
                        val pos = MathUtils.interpolate(0, x_, endX_, 0, 1)
                        startSample = new DataSample(startSample_)
                        startSample.interpolate(pos, endSample_)
                        0
                    } else x_


                    // Clip right side
                    var endSample = endSample_
                    var endX = if (endX_ > width) {
                        val pos = MathUtils.interpolate(width, x_, endX_, 0, 1)
                        endSample = new DataSample(startSample_)
                        endSample.interpolate(pos, endSample_)
                        width
                    } else endX_


                    val startIndex = scanline * width + x
                    val length = endX - x

                    scanlineCalculator.calculateScanline(startSample, endSample, imageData, startIndex, length)

                    updatedArea.includeArea(x, scanline, endX, scanline)
                }
            }

    }

    private def copyData( src : Array[Int], dest : Array[Int] ) {
        val size = width * height
        Platform.arraycopy( src, 0, dest, 0, size )
    }

    def canUndo() : Boolean = !undoQueue.isEmpty
    def canRedo() : Boolean = false
    def undo() {
        if ( canUndo ) {
            val undoData = undoQueue.head

            copyData(undoData, imageData)

            undoQueue = undoQueue.tail

            updatedArea.includeArea(0, 0, width, height)
        }
    }

    def redo() {
        // Not implemented yet
    }

    /**
     * Takes a snapshot of the current surface for the undo queue.
     */
    def undoSnapshot() {

        // If undo queue is over size, reuse last undo data array, otherwise create new
        val undoData = if (undoQueue.size >= undoQueueSize) {
                val lastUndoData = undoQueue.last
                undoQueue = undoQueue.take( undoQueueSize - 1 )
                lastUndoData
            } else {
                try {
                    new Array[Int](width * height)
                }
                catch {
                    case e : Throwable => {e.printStackTrace ; return } // Don't store undo data if we get out of memory exception or similar
                }
            }

        copyData( imageData, undoData )

        undoQueue = undoData :: undoQueue
    }

}