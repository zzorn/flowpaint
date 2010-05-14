package org.flowpaint.model2

import collection.mutable.HashMap

/**
 * Block of data for a quadratic region of pixels of the specified size along the sides,
 * with the pixel data for each channel (if present).
 */
class Block(size: Int) {
  // Map from channel name to data of the channel
  private val data: Map[String, Array[Float]] = new HashMap()
}

