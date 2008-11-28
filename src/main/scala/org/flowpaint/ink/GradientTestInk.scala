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


  def calculateColor(pixelData: DataSample)  {

    val positionAlongStroke = pixelData.getProperty( "positionAlongStroke",0  )
    val positionAcrossStroke = pixelData.getProperty( "positionAcrossStroke",0  )


    val time: Float = pixelData.getProperty("time", 0)

    //val dots = noise.noise2( time*0.1f, positionAcrossStroke*5 )
    val noise = 0.5f+0.5f*util.PerlinNoise.perlinNoise( time*60f, positionAcrossStroke*2.3f, 1.1f )
    val dots2 = util.MathUtils.interpolate(bumpiness, 1f, noise)
    val stripes = 0.25f + 0.25f * Math.sin(time*8).toFloat

    val red = stripes + 0.5f
    val blue = blueParam
    val green = stripes * 0.5f + 0.25f
    val alpha = (1f - Math.abs(positionAcrossStroke)) * dots2 * pixelData.getProperty("alpha", 1)

    pixelData.setProperty( "red", red )
    pixelData.setProperty( "green", green)
    pixelData.setProperty( "blue", blue)
    pixelData.setProperty( "alpha", alpha)

  }
}
