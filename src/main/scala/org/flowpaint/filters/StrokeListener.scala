package org.flowpaint.filters

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

trait StrokeListener extends StrokeFilter {

  protected final def filterStrokePoint(pointData: DataSample, resultSink: (DataSample) => Unit) = {

    addStrokePoint( pointData )

  }

  def addStrokePoint( pointData : DataSample )

}