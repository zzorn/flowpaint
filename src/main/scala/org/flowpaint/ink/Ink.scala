package org.flowpaint.ink

import util.{DataSample, Processor}

/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Ink extends Processor {

  def processPixel( pixelData : DataSample )
  
}