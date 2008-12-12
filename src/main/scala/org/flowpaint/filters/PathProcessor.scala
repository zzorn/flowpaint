package org.flowpaint.filters
import property.Data
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

trait PathProcessor {

  def init( settings : Data )

  def processPathPoint( pathPointData : Data )


}