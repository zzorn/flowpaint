package org.flowpaint.filters

import util.DataSample


import util.PropertyRegister

/**
 * 
 *
 * @author Hans Haggstrom
 */

class RadiusFromPressureFilter(maxRadius : Float, pressureEffect : Float) extends StrokeFilter {

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) = {

    val pressure = pointData.getProperty( PropertyRegister.PRESSURE, 0.5f )
    val maxRadius2 = pointData.getProperty( PropertyRegister.MAX_RADIUS, 10 )

    val radius = util.MathUtils.interpolate( pressureEffect, maxRadius2, pressure * maxRadius2 )

    pointData.setProperty( PropertyRegister.RADIUS, radius )

    resultCallback(pointData)
  }


}