package org.flowpaint.renderer.packedsurface


import java.awt.{Color, Graphics}
import org.flowpaint.property.Data
import org.flowpaint.pixelprocessor.ScanlineCalculator
import org.flowpaint.util.DataSample
import org.flowpaint.renderer.PictureProvider


/**
 * A Surface that provides run length encoded packing of the undo buffer.
 * 
 * @author Hans Haggstrom
 */
class PackedSurface(provider : PictureProvider) /*extends RenderSurface*/ {
  val pictureProvider = provider

  def setViewPortSize(aWidth: Int, aHeight: Int) = null

  def getHeight() = null
  def getWidth() = null

  def isInitialized = false

  def undoSnapshot() = null
  def canRedo() = null
  def canUndo() = null
  def redo() = null
  def undo() = null

  def putPixel(x: Int, y: Int, sample: Data) = null
  def getPixel(x: Int, y: Int) = null

  def clearToColor(color: Color) = null
  def clear() = null

  def provideContent(minX: Float, minY: Float, maxX: Float, maxY: Float, colorCalculator: (Int, Int) => Int) = null

  def renderScanline(scanline: Int, x: Int, endX: Int, startSample: DataSample, endSample: DataSample, scanlineCalculator: ScanlineCalculator) = null

  def renderFullArea(context: Graphics) = null
  def renderChangedArea(context: Graphics) = null


  def getImage = null
  def createBufferedImage = null

}

