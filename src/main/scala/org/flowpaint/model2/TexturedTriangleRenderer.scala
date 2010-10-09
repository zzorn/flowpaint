package org.flowpaint.model2

import raster._

/**
 * Renders a textured triangle to some channel using a triangle area in another channel as the texture source.
 *
 * @author Hans Haggstrom
 */
class TexturedTriangleRenderer {

  private var uDelta: Float = 0f
  private var vDelta: Float = 0f
  private var u: Float = 0f
  private var v: Float = 0f
  private val aTexture = new Vec2f()
  private val bTexture = new Vec2f()

  def renderTriangle(t0: Vec2i, t1: Vec2i, t2: Vec2i,
                     s0: Vec2f, s1: Vec2f, s2: Vec2f,
                     target: Channel, source: Channel) {

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
                        target, source)
    rasterizeTrapetzoid(t1.y, t2.y,
                        t0.x, t0.y, d02, s0, s2, oneOverSteps0to2,  
                        t1.x, t1.y, d12, s1, s2, oneOverSteps1to2,
                        target, source)

  }

  private def rasterizeTrapetzoid(startY: Int, endY: Int,
                          aX: Int, aY: Int, aD: Float, aS: Vec2f, aS2: Vec2f, aDeltaFactor: Float,
                          bX: Int, bY: Int, bD: Float, bS: Vec2f, bS2: Vec2f, bDeltaFactor: Float,
                          target: Channel, source: Channel) {

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
          fillLine(scanline, aXCoord, bXCoord, aTexture, bTexture, target, source);
        }
        else {
          fillLine(scanline, bXCoord, aXCoord, bTexture, aTexture, target, source);
        }
      }

      scanline += 1
    }
  }

  /**
   *    Renders a part of a scanline
   */
  private def fillLine(y: Int, startX : Int, endX : Int,
                       leftTexture: Vec2f, rightTexture: Vec2f,
                       target: Channel, source: Channel) {

    val length = endX - startX
    if (length > 0) {

      if (length == 1) {
        // If start and end are on the same pixel, just average the start and end values
        u = (leftTexture.x + rightTexture.x) * 0.5f
        v = (leftTexture.y + rightTexture.y) * 0.5f
        target.setValueAt(startX, y, source.getAntialiasedValueAt(u, v))
      }
      else {
        u = leftTexture.x
        v = leftTexture.y
        uDelta = (rightTexture.x - leftTexture.x) / length
        vDelta = (rightTexture.y - leftTexture.y) / length

        var x = startX
        while (x < endX) {

          target.setValueAt(x, y, source.getAntialiasedValueAt(u, v))

          u += uDelta
          v += vDelta
          x += 1
        }

      }

    }

//      updatedArea.includeArea(x, scanline, endX, scanline)
  }


}
