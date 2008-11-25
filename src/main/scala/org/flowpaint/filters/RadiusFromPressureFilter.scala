package org.flowpaint.filters

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

class RadiusFromPressureFilter(maxRadius : Float, pressureEffect : Float) extends StrokeFilter {

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) = {

    val pressure = pointData.getProperty( "pressure", 0.5f )
    val maxRadius2 = pointData.getProperty( "maxRadius", 10 )

    val radius = util.MathUtils.interpolate( pressureEffect, maxRadius2, pressure * maxRadius2 )

    pointData.setProperty( "radius", radius )

    resultCallback(pointData)
  }


}