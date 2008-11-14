package org.flowpaint.renderer


/**
 * Represents a surface that can be rendered to.
 *
 * @author Hans Haggstrom
 */
trait RenderSurface {

  def putPixel(x: Int, y: Int, color: Int)

}