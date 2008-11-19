package org.flowpaint.filters

import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

trait StrokeListener  {

  def addStrokePoint( pointData : DataSample )

}