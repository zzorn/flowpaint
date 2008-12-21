package org.flowpaint.gradient


import _root_.org.flowpaint.property.{Data, DataImpl}
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 *
 * @deprecated Use MultiGradient instead, it's more general, but probably about as fast.
 */

class TwoColorGradient(zero : Data, one : Data) extends Gradient {

  protected def gradientValue(zeroToOne: Float): Data = {
    val sample = new DataImpl()

    sample.setValuesFrom( zero )
    sample.interpolate( zeroToOne, one )

    sample
  }

}