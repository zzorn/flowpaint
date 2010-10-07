package org.flowpaint.model2

import layer.Layer

/**
 * 
 */
class Picture {
  var layers: List[Layer] = Nil

  def layer(name: Symbol): Option[Layer] = layers.find(_.identifier == name)
}

