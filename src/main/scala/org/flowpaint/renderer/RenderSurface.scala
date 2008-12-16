package org.flowpaint.renderer


import java.awt.{Graphics2D, Color}
import util.DataSample

/**
 *  Represents a surface that can be rendered to.
 *
 * @author Hans Haggstrom
 */
// TODO: Add transformation / world area that the surface covers
trait RenderSurface {

  val pictureProvider: PictureProvider

  /**
   * Clears the poicture to the default background color
   */
  def clear()

  /**
   * Clears the poicture to the specified color
   */
  def clearToColor( color : Color )

  def setViewPortSize(aWidth: Int, aHeight: Int)

  def render( context : Graphics2D  )

  def width() : Int

  def height() : Int

  def putPixel( x : Int, y : Int, sample : DataSample )

  def provideContent(minX: Float, minY: Float,
                    maxX: Float, maxY: Float,
                    colorCalculator: (Int, Int) => Int)


}