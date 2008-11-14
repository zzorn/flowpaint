package org.flowpaint.brush
/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Brush {
  def calculateColor( positionAlongStroke: Float, positionAcrossStroke: Float): Int
}
