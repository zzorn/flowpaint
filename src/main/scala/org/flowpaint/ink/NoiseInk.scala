package org.flowpaint.ink

import gradient.Gradient
import util.DataSample

import util.PropertyRegister

/**
 *
 *
 * @author Hans Haggstrom
 */

class NoiseInk(gradient: Gradient,
              noiseScale: (Float, Float),
              alphaWithDistance : Float,
              propertyName : String,
              octaves : Int) extends Ink {



  def this(gradient: Gradient,
              noiseScale: (Float, Float),
              alphaWithDistance : Float) {
    this ( gradient, noiseScale, 1, "time", 1 )
  }


  def processPixel(pixelData: DataSample)   {

    val positionAlongStroke = pixelData.getProperty( PropertyRegister.POSTION_ALONG_STROKE,0  )
    val positionAcrossStroke = pixelData.getProperty( PropertyRegister.POSITION_ACROSS_STROKE,0  )

    var u = pixelData.getProperty(propertyName , 1) * noiseScale._1
    var v = positionAcrossStroke * noiseScale._2
    var w = pixelData.getProperty(PropertyRegister.RANDOM_SEED, 0.5f) * 1000

    // Construct the noise from multiple samples (perlin turbulence)
    var n = 0f
    var i = 0
    var scale = 1f
    var amplitude = 1f
    while (i < octaves) {
      n += util.PerlinNoise.perlinNoise(u * scale, v * scale, w) * amplitude
      scale *= 2f
      amplitude *= 0.5f
      u += 213.1234f
      v += 98054.564f
      w += 345.98745f
      i += 1
    }
    
    val noise : DataSample =  gradient( 0.5f + 0.5f * n )

    val noiseAlpha: Float = noise.getProperty(PropertyRegister.ALPHA, 1)
    val distanceFromEdge = 1f - Math.abs(positionAcrossStroke)
    val alphaMul = if( distanceFromEdge >= alphaWithDistance)  1f else distanceFromEdge / alphaWithDistance
    val alpha = noiseAlpha * alphaMul * pixelData.getProperty (PropertyRegister.ALPHA,1)

    pixelData.setValuesFrom( noise )
    pixelData.setProperty( PropertyRegister.ALPHA, alpha)
  }
}
