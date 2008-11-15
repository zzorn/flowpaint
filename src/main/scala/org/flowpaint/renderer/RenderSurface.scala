package org.flowpaint.renderer


import java.awt.Graphics2D

/**
 *  Represents a surface that can be rendered to.
 *
 * @author Hans Haggstrom
 */
trait RenderSurface {

  val pictureProvider: PictureProvider

  /**
   * Clears the poicture to the default background color
   */
  def clear()

  def setViewPortSize(aWidth: Int, aHeight: Int)

  def render( context : Graphics2D  )

  def provideContent(minX: Float, minY: Float,
                    maxX: Float, maxY: Float,
                    colorCalculator: (Int, Int) => Int)

}