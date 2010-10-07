package org.flowpaint.model2

/**
 * 
 */
case class Vec2i(var x: Int, var y: Int) {

  def swap(other: Vec2i) {
    val tx = other.x
    val ty = other.y
    other.x = x
    other.y = y
    x = tx
    y = ty
  }
}
