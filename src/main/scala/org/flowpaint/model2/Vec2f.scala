package org.flowpaint.model2

/**
 * 
 */
case class Vec2f(var x: Float, var y: Float) {

  def this() {
    this(0,0)
  }
  
  def swap(other: Vec2f) {
    val tx = other.x
    val ty = other.y
    other.x = x
    other.y = y
    x = tx
    y = ty
  }

  def interpolate(t: Float, a: Vec2f, b: Vec2f) {
    val invT = 1f - t
    x = a.x * invT + b.x * t
    y = a.y * invT + b.y * t
  }

}