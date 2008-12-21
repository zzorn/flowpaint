package org.flowpaint.renderer

import property.{DataImpl, Data}
import util.DataSample

/**
 *        Renders a triangle
 *
 * @author Hans Haggstrom
 */
class TriangleRenderer {
  type PixelCallback = (Int, Int, Data) => Unit

    val RED_COLOR = new DataSample( ("red", 1f), ("alpha", 1f) )

  private val pixelSample = new DataSample()
  private val aSample = new DataSample()
  private val bSample = new DataSample()

    private val tempData = new DataImpl()

  def renderTriangle(viewWidth: Int, viewHeight: Int,
                    xi0: Float, yi0: Float, xi1: Float, yi1: Float, xi2: Float, yi2: Float,
                    dataSample0: DataSample, dataSample1: DataSample, dataSample2: DataSample,
                    pixelCallback: PixelCallback) {


    /**
     *    Renders a part of a scanline
     */
    def fillLine(scanline: Int, x_ : Int, endX_ : Int, startSample: DataSample, endSample: DataSample) {

      // Clip top and bottom and off screen
      if (scanline >= 0 && scanline < viewHeight && x_ < viewWidth && endX_ >= 0 && endX_ > x_) {

        // Clip left side
        var x = if (x_ < 0) 0 else x_

        // Clip right side
        val endX = if (endX_ > viewWidth) viewWidth else endX_

        // Precalculate one over length of line
        val divisor = 1f / (endX_ - x_).toFloat

        // Render scanline
        while (x < endX) {
          val positionAlongLine: Float = (x - x_).toFloat * divisor

          pixelSample.interpolate( positionAlongLine,  startSample, endSample )

          tempData.clear
          tempData.setFloatProperties(pixelSample)

          pixelCallback(x, scanline, tempData)
          x += 1
        }
      }
    }

    def rasterizeTrapetzoid(startY: Int, endY: Int,
                           aX: Int, aY: Int, aD: Float, aS: DataSample, aS2: DataSample, aDeltaFactor: Float,
                           bX: Int, bY: Int, bD: Float, bS: DataSample, bS2: DataSample, bDeltaFactor: Float) {

      var y = 0f
      var aXCoord: Int = 0
      var bXCoord: Int = 0
      var scanline: Int = startY

      while (scanline < endY) {
        y = scanline.toFloat
        val linesFromAStart: Float = y - aY
        val linesFromBStart: Float = y - bY

        // OPTIMIZE: We could get rid of a multiplication if we add the delta instead.
        // But could eat a bit of precision maybe?
        aXCoord = (aX + (linesFromAStart * aD)).toInt
        bXCoord = (bX + (linesFromBStart * bD)).toInt

        if (aXCoord != bXCoord) {
          aSample.interpolate( linesFromAStart * aDeltaFactor, aS, aS2 )
          bSample.interpolate( linesFromBStart * bDeltaFactor, bS, bS2 )

          if (aXCoord < bXCoord) {
            fillLine(scanline, aXCoord, bXCoord, aSample, bSample);
          }
          else {
            fillLine(scanline, bXCoord, aXCoord, bSample, aSample);
          }
        }

        scanline += 1
      }
    }


    // Round to ints, or we get rendering artefacts
    var x0 = xi0.toInt
    var x1 = xi1.toInt
    var x2 = xi2.toInt

    var y0 = yi0.toInt
    var y1 = yi1.toInt
    var y2 = yi2.toInt

    var s0 = dataSample0
    var s1 = dataSample1
    var s2 = dataSample2

    // Sort points according to y coordinate so that y0 <= y1 <= y2
    var tempI: Int = 0
    var tempS: DataSample = null
    if (y1 < y0) {
      // Swap point 0 and 1
      tempI = y0;y0 = y1;y1 = tempI;
      tempI = x0;x0 = x1;x1 = tempI;
      tempS = s0;s0 = s1;s1 = tempS;
    }
    if (y2 < y1) {
      // Swap point 1 and 2
      tempI = y1;y1 = y2;y2 = tempI;
      tempI = x1;x1 = x2;x2 = tempI;
      tempS = s1;s1 = s2;s2 = tempS;
    }
    if (y1 < y0) {
      // Swap point 0 and 1 again
      tempI = y0;y0 = y1;y1 = tempI;
      tempI = x0;x0 = x1;x1 = tempI;
      tempS = s0;s0 = s1;s1 = tempS;
    }

    // Calculate slope coefficients
    def calcOneOver(ya: Int, yb: Int): Float = if (ya - yb == 0) 0f else 1f / (ya - yb).toFloat
    val oneOverSteps0to1 = calcOneOver(y1, y0)
    val oneOverSteps1to2 = calcOneOver(y2, y1)
    val oneOverSteps0to2 = calcOneOver(y2, y0)

    def calculateCoefficient(dx: Int, oneOver: Float): Float = dx.toFloat * oneOver
    val d01: Float = calculateCoefficient(x1 - x0, oneOverSteps0to1)
    val d12: Float = calculateCoefficient(x2 - x1, oneOverSteps1to2)
    val d02: Float = calculateCoefficient(x2 - x0, oneOverSteps0to2)

/*
    def checkGradient( d : Float, y : Int, x:Int, targetY:Int, expectedX:Int) {
      val calculatedX: Float = d * (targetY - y) + x
      if ( calculatedX == expectedX ) println ("gradient ok") else println ("Gradient fail: calculatedX = " + calculatedX + ", expected x: " + expectedX)
    }
    checkGradient(d01, y0, x0, y1, x1)
    checkGradient(d02, y0, x0, y2, x2)
    checkGradient(d12, y1, x1, y2, x2)
*/


/*
    def calculateSampleDelta(s: DataSample, sa: DataSample, sb: DataSample, multiplicand: Float) {
      s.clear()
      s.setValuesFrom(sb)
      s -= sa
      s *= multiplicand
    }
    calculateSampleDelta(sd01, s0, s1, oneOverSteps0to1)
    calculateSampleDelta(sd02, s0, s2, oneOverSteps0to2)
    calculateSampleDelta(sd12, s1, s2, oneOverSteps1to2)
*/

    // Render the upper and lower part of the triangle (above and below the middle y point)
    rasterizeTrapetzoid(y0, y1, x0, y0, d02, s0,s2, oneOverSteps0to2, x0, y0, d01, s0, s1, oneOverSteps0to1)
    rasterizeTrapetzoid(y1, y2, x0, y0, d02, s0, s2, oneOverSteps0to2, x1, y1, d12, s1, s2, oneOverSteps1to2)


/*
    pixelCallback( xi0.toInt, yi0.toInt, RED_COLOR  )
    pixelCallback( xi1.toInt, yi1.toInt, RED_COLOR  )
    pixelCallback( xi2.toInt, yi2.toInt, RED_COLOR  )
*/
  }


}
