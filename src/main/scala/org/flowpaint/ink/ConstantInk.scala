package org.flowpaint.ink
import _root_.org.flowpaint.property.Data
import util.DataSample

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