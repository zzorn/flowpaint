package org.flowpaint.model2

import _root_.java.util.ArrayList


/**
 * Contains located data points, that can be linked to each other to form vector graphics objects.
 * The data itself is not visible, but can be visualized in various ways.
 */
class DataMap {

  // TODO: Rtree or similar fast location data hash?
  var data: ArrayList[Data] = new ArrayList()

}

