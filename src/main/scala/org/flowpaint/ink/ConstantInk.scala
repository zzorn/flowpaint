package org.flowpaint.ink
import util.DataSample

/**
 * An ink that sets some of the ink properties to fixed values.
 * 
 * @author Hans Haggstrom
 */
class ConstantInk(values : DataSample)  extends Ink {


  def processPixel(pixelData: DataSample) {

    pixelData.setValuesFrom( values )

  }
}