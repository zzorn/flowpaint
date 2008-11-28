package org.flowpaint.ink

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Ink {
  def calculateColor( pixelData : DataSample )
  
}