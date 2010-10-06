package org.flowpaint.model2

import _root_.java.awt.image.BufferedImage
import collection.mutable.HashMap
import collection._
import raster.{FloatTile, Tile}
import org.flowpaint.util.Rectangle

/**
 * Block of data for a quadratic region of pixels of the specified size along the sides,
 * with the pixel data for each channel (if present).
 */
// TODO: Use own memory management for these with weak references or such, as long as the size is constant
case class Block(width: Int, height: Int, canvasX: Int, canvasY: Int) extends Rectangle {
  require(width > 0, "Width should be positive")
  require(height > 0, "Height should be positive")

  def x1 = canvasX
  def y1 = canvasY

  // Map from channel name to data of the channel
  private val channelTiles: mutable.Map[Symbol, Tile] = new HashMap()

  def addChannel(name: Symbol): Tile = {
    val channel: Tile = new FloatTile(width, height)
    channelTiles(name) = channel
    channel
  }

  def channel(name: Symbol): Tile = channelTiles(name)

  def copyToRgbImage(imageData: Array[Int], imageWidth: Int, area: Rectangle,
                  alphaGridSize: Int = 16, alphaLuminance1 : Float = 0.5f, alphaLuminance2: Float = 0.7f) {
    val red: Tile = channel('red)
    val green: Tile = channel('green)
    val blue: Tile = channel('blue)
    val alpha: Tile = channel('alpha)

    val renderArea: Rectangle = union(area)
    val vy1: Int = renderArea.y1
    val vx2: Int = renderArea.x2
    val vy2: Int = renderArea.y2
    val vx1: Int = renderArea.x1

    var y = vy1
    var x = 0
    var index = 0
    var imageIndex = 0
    while(y < vy2) {

      x = vx1
      imageIndex = y * imageWidth + x
      while(x < vx2) {

        val a = alpha.getFloat(index)
        val colorCode: Int = if (a < 1f) {
          // Calculate checkerboard alpha pattern if we have some transparency
          val preMultipliedAlphaLuminance = (1f - a) * (if (! ((x % alphaGridSize * 2 < alphaGridSize) == (y % alphaGridSize * 2 < alphaGridSize))) alphaLuminance1 else alphaLuminance2)

          val r = (255 * (preMultipliedAlphaLuminance + red(index)   * a)).toInt
          val g = (255 * (preMultipliedAlphaLuminance + green(index) * a)).toInt
          val b = (255 * (preMultipliedAlphaLuminance + blue(index)  * a)).toInt

          0xFF000000 | (r << 16) | (g << 8) | b
        }
        else {
          0xFF000000 | (red.getByte(index) << 16) | (green.getByte(index) << 8) | blue.getByte(index)
        }

        imageData(imageIndex) = colorCode

        x += 1
        index += 1
        imageIndex += 1
      }

      y += 1
    }

  }


  def copyToRgbaImage(imageData: Array[Int], imageWidth: Int, area: Rectangle) {
    val red: Tile = channel('red)
    val green: Tile = channel('green)
    val blue: Tile = channel('blue)
    val alpha: Tile = channel('alpha)

    val renderArea: Rectangle = union(area)
    val vy1: Int = renderArea.y1
    val vx2: Int = renderArea.x2
    val vy2: Int = renderArea.y2
    val vx1: Int = renderArea.x1

    var y = vy1
    var x = 0
    var index = 0
    var imageIndex = 0
    while(y < vy2) {

      x = vx1
      imageIndex = y * imageWidth + x
      while(x < vx2) {

        imageData(imageIndex) = (alpha.getByte(index) << 24) | (red.getByte(index) << 16) | (green.getByte(index) << 8) | blue.getByte(index)

        x += 1
        index += 1
        imageIndex += 1
      }

      y += 1
    }

  }


}

