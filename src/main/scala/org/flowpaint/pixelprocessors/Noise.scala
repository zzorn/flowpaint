package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import java.lang.String
import pixelprocessor.PixelProcessor
import util.{DataSample, PerlinNoise}


/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Noise extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val NOISE1: String = "noisePosition1"
    val NOISE2: String = "noisePosition2"
    val NOISE3: String = "noisePosition3"

    val x = getScaleOffsetVar( NOISE1, 0, variables, variableNameMappings )
    val y = getScaleOffsetVar( NOISE2, 0, variables, variableNameMappings )
    val z = getScaleOffsetVar( NOISE3, 0, variables, variableNameMappings )

    val inputs = if ( hasMappedVar( NOISE3, variables, variableNameMappings ) ) 3
                else if( hasMappedVar( NOISE2, variables, variableNameMappings ) ) 2
                else if( hasMappedVar( NOISE1, variables, variableNameMappings ) ) 1
                else 0

    val noise = inputs match {
      case 0 => 0f
      case 1 => PerlinNoise.noise1( x )
      case 2 => PerlinNoise.noise2( x, y )
      case 3 => PerlinNoise.noise3( x, y, z )
    }

    val result = noise * 0.5f + 0.5f // Transform to range 0..1, as most variables such as R,G,B etc are in that range. 

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }
}