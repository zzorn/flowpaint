package org.flowpaint.ink

import util.DataSample

/**
 * Used for debugging stroke rendering.
 * 
 * @author Hans Haggstrom
 */
class DebugInk( alongFactor : Float, acrossFactor : Float ) extends Ink {

  def processPixel(pixelData: DataSample) = {

    val positionAlongStroke = pixelData.getProperty( "positionAlongStroke",0  )
    val positionAcrossStroke = pixelData.getProperty( "positionAcrossStroke",0  )

    pixelData.setProperty( "red", positionAlongStroke * alongFactor)
    pixelData.setProperty( "green", 0f )
    pixelData.setProperty( "blue", (0.5f + 0.5f * positionAcrossStroke) * acrossFactor)
    pixelData.setProperty( "alpha", 1 )


  }
}