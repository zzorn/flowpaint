package org.flowpaint.brush

import ink.Ink
import util.DataSample

/**
 *   Just a simple test brush.
 *
 * @author Hans Haggstrom
 */
class GradientTestInk extends Ink {

  var blueParam = 0f
  var bumpiness = 1f

  def this( blue : Float, bumpiness : Float ) {
    this()
    blueParam = blue
    this.bumpiness = bumpiness
  }


  def calculateColor(positionAlongStroke: Float, positionAcrossStroke: Float,
                    startData: DataSample, endData: DataSample): Int = {



    def getInterpolatedProperty( name:String, default:Float ) :Float = {
        util.MathUtils.interpolate(
      positionAlongStroke,
      startData.getProperty(name, default),
      endData.getProperty(name, default))
    }

    val time: Float = getInterpolatedProperty("time", 0)

    //val dots = noise.noise2( time*0.1f, positionAcrossStroke*5 )
    val noise = 0.5f+0.5f*util.PerlinNoise.perlinNoise( time*60f, positionAcrossStroke*2.3f, 1.1f )
    val dots2 = util.MathUtils.interpolate(bumpiness, 1f, noise)
    val stripes = 0.25f + 0.25f * Math.sin(time*8).toFloat

    val red = stripes + 0.5f
    val blue = blueParam
    val green = stripes * 0.5f + 0.25f

    val alpha = (1f - Math.abs(positionAcrossStroke)) * dots2

    val r = (255 * red).toInt
    val g = (255 * green).toInt
    val b = (255 * blue).toInt
    val a = (255 * alpha).toInt

    val color = ((a & 0xFF) << 24) |
            ((r & 0xFF) << 16) |
            ((g & 0xFF) << 8) |
            ((b & 0xFF) << 0);

    color
  }
}
