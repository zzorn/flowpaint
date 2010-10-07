package org.flowpaint.model2

import org.flowpaint.pixelprocessor.ScanlineCalculator
import org.flowpaint.property.{DataImpl, Data}
import raster.Tile
import org.flowpaint.util.{MathUtils, Rectangle, DataSample, PropertyRegister}

/**
 *        Renders a triangle
 *
 * @author Hans Haggstrom
 */
class TexturedTriangleRenderer {

  private val aTexture = new Vec2f()
  private val bTexture = new Vec2f()
  private var uDelta: Float = 0f
  private var vDelta: Float = 0f
  private var u: Float = 0f
  private var v: Float = 0f

  def renderTriangle(t0: Vec2i, t1: Vec2i, t2: Vec2i,
                     s0: Vec2f, s1: Vec2f, s2: Vec2f,
                     target: Tile, source: Tile,
                     area: Rectangle, sourceArea: Rectangle) {

    // Sort points according to y coordinate so that y0 <= y1 <= y2
    if (t1.y < t0.y) {
      // Swap point 0 and 1
      t0 swap t1
      s0 swap s1
    }
    if (t2.y < t1.y) {
      // Swap point 1 and 2
      t1 swap t2
      s1 swap s2
    }
    if (t1.y < t0.y) {
      // Swap point 0 and 1 again
      t0 swap t1
      s0 swap s1
    }

    // Calculate slope coefficients
    def calcOneOver(ya: Int, yb: Int): Float = if (ya - yb == 0) 0f else 1f / (ya - yb).toFloat
    val oneOverSteps0to1 = calcOneOver(t1.y, t0.y)
    val oneOverSteps1to2 = calcOneOver(t2.y, t1.y)
    val oneOverSteps0to2 = calcOneOver(t2.y, t0.y)

    def calculateCoefficient(dx: Int, oneOver: Float): Float = dx.toFloat * oneOver
    val d01: Float = calculateCoefficient(t1.x - t0.x, oneOverSteps0to1)
    val d12: Float = calculateCoefficient(t2.x - t1.x, oneOverSteps1to2)
    val d02: Float = calculateCoefficient(t2.x - t0.x, oneOverSteps0to2)

    // Render the upper and lower part of the triangle (above and below the middle y point)
    rasterizeTrapetzoid(t0.y, t1.y,
                        t0.x, t0.y, d02, s0, s2, oneOverSteps0to2,
                        t0.x, t0.y, d01, s0, s1, oneOverSteps0to1,
                        target, source, area, sourceArea)
    rasterizeTrapetzoid(t1.y, t2.y,
                        t0.x, t0.y, d02, s0, s2, oneOverSteps0to2,  
                        t1.x, t1.y, d12, s1, s2, oneOverSteps1to2,
                        target, source, area, sourceArea)

  }

  private def rasterizeTrapetzoid(startY: Int, endY: Int,
                          aX: Int, aY: Int, aD: Float, aS: Vec2f, aS2: Vec2f, aDeltaFactor: Float,
                          bX: Int, bY: Int, bD: Float, bS: Vec2f, bS2: Vec2f, bDeltaFactor: Float,
                          target: Tile, source: Tile, area: Rectangle, sourceArea: Rectangle) {

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
        aTexture.interpolate( linesFromAStart * aDeltaFactor, aS, aS2 )
        bTexture.interpolate( linesFromBStart * bDeltaFactor, bS, bS2 )

        if (aXCoord < bXCoord) {
          fillLine(scanline, aXCoord, bXCoord, aTexture, bTexture, target, source, area, sourceArea);
        }
        else {
          fillLine(scanline, bXCoord, aXCoord, bTexture, aTexture, target, source, area, sourceArea);
        }
      }

      scanline += 1
    }
  }

  /**
   *    Renders a part of a scanline
   */
  private def fillLine(scanline: Int, x_ : Int, endX_ : Int, leftTexture: Vec2f, rightTexture: Vec2f,
                       target: Tile, source: Tile, area: Rectangle, sourceArea: Rectangle) {

    val tileY = scanline - area.y1

    startSample.setFloatProperty( PropertyRegister.CANVAS_X, x_ )
    rightTexture.setFloatProperty( PropertyRegister.CANVAS_X, endX_ )
    leftTexture.setFloatProperty( PropertyRegister.CANVAS_Y, scanline )
    rightTexture.setFloatProperty( PropertyRegister.CANVAS_Y, scanline )

    surface.renderScanline(
        scanline,
        x_,
        endX_,
        leftTexture.getFloatProperties,
        rightTexture.getFloatProperties,
        scanlineCalculator )

    // Clip top and bottom and off screen
    if (scanline >= area.y1 && scanline < area.y2 && x_ < area.x2 && endX_ >= area.x1 && endX_ > x_) {

      // Clip left side
      var startSample = leftTexture
      var x = if (x_ < area.x1) {
        val pos = MathUtils.interpolate(area.x1, x_, endX_, 0, 1)
        leftTexture.interpolate(pos, leftTexture, rightTexture)
        area.x1
      } else x_


      // Clip right side
      var endSample = rightTexture
      var endX = if (endX_ >= area.x2) {
        val pos = MathUtils.interpolate(area.x2, x, endX_, 0, 1)
        leftTexture.interpolate(pos, leftTexture, rightTexture)
        area.x2
      } else endX_


      val length = endX - x
      var index = (scanline - area.y1) * area.width + (x - area.x1)
      val endIndex = index + length
      scanlineCalculator.calculateScanline(startSample, endSample, imageData, startIndex, length)

      if (length > 0) {


        // Get start and end values for variables
        copyInitialVariables(variablesAtScanlineStart, start)
        copyInitialVariables(variableValueDeltas, end)

        // NOTE: By passing in initial value and delta (instead of value at start and end),
        // we make it easier to later use the same pixel program code to generate only the parts
        // of scanlines that fall within some screen surface block

        // Initialize the deltas to apply each step
        if (length == 1) {
          // If start and end are on the same pixel, just average the start and end values
          u = (leftTexture.x + rightTexture.x) * 0.5f
          v = (leftTexture.y + rightTexture.y) * 0.5f

        }
        else {
          u = leftTexture.x
          v = leftTexture.y
          uDelta = (rightTexture.x - leftTexture.x) / length
          vDelta = (rightTexture.y - leftTexture.y) / length

          while (index < endIndex) {

            target.setFloat(index, source.)

            u += uDelta
            v += vDelta
            index += 1
          }

        }

        // Render the scanline
        program.calculateScanline( variablesAtScanlineStart,
                                      variableValueDeltas,
                                      variableWorkingValues,
                                      outputBuffer,
                                      outputOffset,
                                      scanlineLength )
      }

//      updatedArea.includeArea(x, scanline, endX, scanline)
    }
  }


}
