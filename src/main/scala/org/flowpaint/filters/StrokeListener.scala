package org.flowpaint.filters

import org.flowpaint.util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

trait StrokeListener  {

  def addStrokePoint( pointData : DataSample )

}