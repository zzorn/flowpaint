package org.flowpaint.pixelprocessor
import _root_.org.flowpaint.brush.Brush
import model.Path
import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

abstract class ScanlineCalculator  {

  private var program : PixelProgram = null

  protected def createProgram : PixelProgram

  def init( path : Path )

  def calculateScanline( start : DataSample, end : DataSample,
                       outputBuffer : Array[Int],
                       outputOffset : Int,
                       scanlineLength : Int )
  
}