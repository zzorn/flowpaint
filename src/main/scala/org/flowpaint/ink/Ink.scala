package org.flowpaint.ink

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Ink {
  def calculateColor( positionAlongStroke: Float, positionAcrossStroke: Float,
                    startData : DataSample, endData : DataSample ): Int
  
}