package org.flowpaint.brush

import _root_.scala.collection.jcl.ArrayList
import filters.{StrokeListener, StrokeFilter}
import ink.Ink
import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */
case class Brush( ink : Ink, filters : List[StrokeFilter] ) {

  def filterStrokePoint(pointData:DataSample,listener: StrokeListener )  {

    if (!filters.isEmpty) {

      filters.head.filterStrokePoint( pointData, filters.tail,listener )
    }


  }

}
