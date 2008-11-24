package org.flowpaint.gradient

import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 *
 * @deprecated Use MultiGradient instead, it's more general, but probably about as fast.
 */

class TwoColorGradient(zero : DataSample, one : DataSample) extends Gradient {

  protected def gradientValue(zeroToOne: Float): DataSample = {
    val sample = new DataSample()

    sample.setValuesFrom( zero )
    sample.interpolate( zeroToOne, one )

    sample
  }

}