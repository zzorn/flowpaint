package org.flowpaint.ink

import _root_.org.flowpaint.property.Data
import util.{DataSample, Processor}

/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Ink extends Processor {

  def processPixel( pixelData : Data )
  
}