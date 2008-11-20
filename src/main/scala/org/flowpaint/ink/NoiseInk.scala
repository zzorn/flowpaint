package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

class NoiseInk(gradient: Gradient,
              noiseScale: (Float, Float),
              randomSeed: Long,
              propertyName : String,
              resultMapper: (Float) => Float ) extends Ink {


  def this(gradient: Gradient,
              noiseScale: (Float, Float)) {
    this ( gradient, noiseScale,13351L, "time", (a:Float) => a )
  }

  val w = {
    val random = new Random(randomSeed)
    random.nextFloat * 1000
  }


  def calculateColor(positionAlongStroke: Float,
                    positionAcrossStroke: Float,
                    startData: DataSample,
                    endData: DataSample): Int = {


    def interpolatedProperty(name: String, default: Float): Float = {
      util.MathUtils.interpolate(
        positionAlongStroke,
        startData.getProperty(name, default),
        endData.getProperty(name, default))
    }

    val u = interpolatedProperty(propertyName , 1) * noiseScale._1
    val v = positionAcrossStroke * noiseScale._2

    val noise : DataSample =  gradient( resultMapper( 0.5f + 0.5f * util.PerlinNoise.perlinNoise(u, v, w) ) )

    val red = noise.getProperty("red", 0)
    val green = noise.getProperty("green", 0)
    val blue= noise.getProperty("blue", 0)
    val alpha= noise.getProperty("alpha", 1) * (1f - Math.abs(positionAcrossStroke))

    util.ColorUtils.createRGBAColor( red, green, blue, alpha )
  }
}
