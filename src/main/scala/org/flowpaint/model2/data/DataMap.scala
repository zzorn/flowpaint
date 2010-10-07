package org.flowpaint.model2

import _root_.java.util.ArrayList
import _root_.org.flowpaint.util.Rectangle
import collection.mutable.HashMap


/**
 * Contains located data points, that can be linked to each other to form vector graphics objects.
 * The data itself is not visible, but can be visualized in various ways.
 */
class DataMap {

  // TODO: Rtree or similar fast location data hash?
  val dataEntries: ArrayList[Data] = new ArrayList()

  def add(data: Data) {
    dataEntries.add(data)
  }

  def remove(data: Data) {
    dataEntries.remove(data)
  }

  /**
   * Adds the specified data on top of this raster, for the specified area.
   */
  def overlay(dataMap: DataMap, area: Rectangle) {
    dataEntries.addAll(dataMap.dataEntries)
  }

}

