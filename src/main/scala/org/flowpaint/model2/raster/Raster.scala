package org.flowpaint.model2

import collection.mutable.HashMap

/**
 * 
 */

class Raster {

  // The channels present in this raster
  private var channels: List[String] = Nil
  
  // Map from row indexes to map from column indexes to blocks of pixel data.
  private val blocks: Map[Int, Map[Int, Block]] = new HashMap()


}

