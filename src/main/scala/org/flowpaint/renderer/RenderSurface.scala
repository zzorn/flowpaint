package org.flowpaint.renderer


import org.flowpaint.property.Data
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Graphics, Image, Color}
import org.flowpaint.pixelprocessor.ScanlineCalculator
import org.flowpaint.util.DataSample

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

  def renderFullArea( context : Graphics  )

  def renderChangedArea( context : Graphics  )

  def isInitialized : Boolean

  def getImage : Image

  def createBufferedImage : BufferedImage

  def getWidth() : Int

  def getHeight() : Int

  def putPixel( x : Int, y : Int, sample : Data )

  /**
   * Returns the colorcode for the pixel at the specified location, or -1 if not available.
   */
  def getPixel(x : Int, y : Int) : Int


  def provideContent(minX: Float, minY: Float, maxX: Float, maxY: Float,
                    colorCalculator: (Int, Int) => Int)

  def renderScanline(
        scanline : Int,
        x : Int,
        endX : Int,
        startSample : DataSample,
        endSample : DataSample,
        scanlineCalculator : ScanlineCalculator )

  def canUndo() : Boolean
  def canRedo() : Boolean
  def undo()
  def redo()

  /**
   * Takes a snapshot of the current surface for the undo queue.
   */
  def undoSnapshot()


}

