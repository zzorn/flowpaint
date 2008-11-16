package org.flowpaint.filters

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

class ColorUndulationFilter extends StrokeFilter {

  protected def filterStrokePoint(pointData: DataSample, resultCallback: (DataSample) => Unit) = {

    val time = pointData.getProperty( "time", 0f )

    pointData.setProperty( "red", 1 )
    pointData.setProperty( "blue", 0 )
    pointData.setProperty( "green", (0.5 + 0.5 * Math.sin(time*2)).toFloat )

    resultCallback(pointData)

  }


}