package org.flowpaint.ink

import org.flowpaint.property.Data
import org.flowpaint.util.{DataSample, Configuration}

/**
 * 
 *
 * @author Hans Haggstrom
 */
trait Ink extends Configuration {

  def processPixel( pixelData : Data )
  
}