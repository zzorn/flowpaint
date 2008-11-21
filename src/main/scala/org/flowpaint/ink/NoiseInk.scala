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
              propertyName : String,
              resultMapper: (Float) => Float ) extends Ink {


/*
  def this(gradient: Gradient,
              noiseScale: (Float, Float)) {
    this ( gradient, noiseScale,13351L, "time", (a:Float) => a )
  }
*/

  var alphaWithDistance= 1f

  def this(gradient: Gradient,
              noiseScale: (Float, Float),
              alphaWithDistance : Float) {
    this ( gradient, noiseScale, "time", (a:Float) => a )
    this.alphaWithDistance = alphaWithDistance
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
    val w = startData.getProperty("randomSeed", 0.5f) * 1000

    
    val noise : DataSample =  gradient( resultMapper( 0.5f + 0.5f * util.PerlinNoise.perlinNoise(u, v, w) ) )

    val noiseAlpha: Float = noise.getProperty("alpha", 1)


    val distanceFromEdge = 1f - Math.abs(positionAcrossStroke)
    val alphaMul = if( distanceFromEdge >= alphaWithDistance)  1f else distanceFromEdge / alphaWithDistance

    val red = noise.getProperty("red", 0)
    val green = noise.getProperty("green", 0)
    val blue= noise.getProperty("blue", 0)
    val alpha = noiseAlpha * alphaMul

    util.ColorUtils.createRGBAColor( red, green, blue, alpha )
  }
}
