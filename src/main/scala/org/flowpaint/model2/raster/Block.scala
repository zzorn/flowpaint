package org.flowpaint.model2

import _root_.java.awt.image.BufferedImage
import collection.mutable.HashMap

/**
 * Block of data for a quadratic region of pixels of the specified size along the sides,
 * with the pixel data for each channel (if present).
 */
// TODO: Use own memory management for these with weak references or such, as long as the size is constant
case class Block(size: Int, cellX: Int, cellY: Int, x: Int, y: Int) {
  
  // Map from channel name to data of the channel
  private val data: Map[Symbol, Array[Float]] = new HashMap()
  
  def channel(name: Symbol): Array[Float] = data.get(name)

  def render(image: BufferedImage,
             x: Double = 0,
             y: Double = 0,
             scaleX: Double = 1,
             scaleY: Double = 1,
             redChannel: Symbol = 'red,
             greenChannel: Symbol = 'green,
             blueChannel: Symbol = 'blue,
             alphaChannel: Symbol = 'alpha) {
    val red = data.get(redChannel)
    val green = data.get(greenChannel)
    val blue = data.get(blueChannel)
    val alpha = data.get(alphaChannel)

    if (red.isEmpty && green.isEmpty && blue.isEmpty && alpha.isEmpty) return

    // TODO: Replace nonexisting channels with a constant zero channel for the purpose of the algorithm? (should be of correct size thou)

    // TODO: Loop through the destination points, transform and scale source
    // TODO: This requires knowing the neighbour blocks to handle antialiasing......  So maybe remove scaling and such completely from this step?
    // But it would be nice to get it to work..  Will be needed for live updating as the user paints too.

  }
}

