package org.flowpaint.gradient

import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class TwoColorGradient(zero : DataSample, one : DataSample) extends Gradient {

  protected def gradientValue(zeroToOne: Float): DataSample = {
    val sample = new DataSample()

    sample.setValuesFrom( zero )
    sample.interpolate( zeroToOne, one )

    sample
  }

}