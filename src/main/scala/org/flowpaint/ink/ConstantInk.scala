package org.flowpaint.ink
import org.flowpaint.property.Data
import org.flowpaint.util.DataSample

/**
 * An ink that sets some of the ink properties to fixed values.
 * 
 * @author Hans Haggstrom
 */
class ConstantInk()  extends Ink {

  def processPixel(pixelData: Data) {

    pixelData.setValuesFrom( getSettings )

  }
}