package org.flowpaint.filters

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

class RadiusFromPressureFilter(maxRadius : Float) extends StrokeFilter {

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) = {

    val pressure = pointData.getProperty( "pressure", 0.5f )

    pointData.setProperty( "radius", pressure * maxRadius )

    resultCallback(pointData)
  }


}