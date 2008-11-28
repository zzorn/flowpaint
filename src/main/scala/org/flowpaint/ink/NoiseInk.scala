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


  var alphaWithDistance= 1f

  def this(gradient: Gradient,
              noiseScale: (Float, Float),
              alphaWithDistance : Float) {
    this ( gradient, noiseScale, "time", (a:Float) => a )
    this.alphaWithDistance = alphaWithDistance
  }


  def calculateColor(pixelData: DataSample)  {

    val positionAlongStroke = pixelData.getProperty( "positionAlongStroke",0  )
    val positionAcrossStroke = pixelData.getProperty( "positionAcrossStroke",0  )

    val u = pixelData.getProperty(propertyName , 1) * noiseScale._1
    val v = positionAcrossStroke * noiseScale._2
    val w = pixelData.getProperty("randomSeed", 0.5f) * 1000

    
    val noise : DataSample =  gradient( resultMapper( 0.5f + 0.5f * util.PerlinNoise.perlinNoise(u, v, w) ) )

    val noiseAlpha: Float = noise.getProperty("alpha", 1)
    val distanceFromEdge = 1f - Math.abs(positionAcrossStroke)
    val alphaMul = if( distanceFromEdge >= alphaWithDistance)  1f else distanceFromEdge / alphaWithDistance
    val alpha = noiseAlpha * alphaMul * pixelData.getProperty ("alpha",1)

    pixelData.setValuesFrom( noise )
    pixelData.setProperty( "alpha", alpha)
  }
}
