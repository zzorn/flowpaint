package org.flowpaint.ink

import _root_.org.flowpaint.property.Data
import util.{DataSample, Configuration}

/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Ink extends Configuration {

  def processPixel( pixelData : Data )
  
}