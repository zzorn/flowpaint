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
              propertyName : String,
              resultMapper: (Float) => Float ) extends Ink {


  var alphaWithDistance= 1f

  def this(gradient: Gradient,
              noiseScale: (Float, Float),
              alphaWithDistance : Float) {
    this ( gradient, noiseScale, "time", (a:Float) => a )
    this.alphaWithDistance = alphaWithDistance
  }


  def processPixel(pixelData: DataSample)   {

    val positionAlongStroke = pixelData.getProperty( PropertyRegister.POSTION_ALONG_STROKE,0  )
    val positionAcrossStroke = pixelData.getProperty( PropertyRegister.POSITION_ACROSS_STROKE,0  )

    val u = pixelData.getProperty(propertyName , 1) * noiseScale._1
    val v = positionAcrossStroke * noiseScale._2
    val w = pixelData.getProperty(PropertyRegister.RANDOM_SEED, 0.5f) * 1000

    
    val noise : DataSample =  gradient( resultMapper( 0.5f + 0.5f * util.PerlinNoise.perlinNoise(u, v, w) ) )

    val noiseAlpha: Float = noise.getProperty(PropertyRegister.ALPHA, 1)
    val distanceFromEdge = 1f - Math.abs(positionAcrossStroke)
    val alphaMul = if( distanceFromEdge >= alphaWithDistance)  1f else distanceFromEdge / alphaWithDistance
    val alpha = noiseAlpha * alphaMul * pixelData.getProperty (PropertyRegister.ALPHA,1)

    pixelData.setValuesFrom( noise )
    pixelData.setProperty( PropertyRegister.ALPHA, alpha)
  }
}
