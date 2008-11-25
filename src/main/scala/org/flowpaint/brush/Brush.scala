package org.flowpaint.brush

import _root_.scala.collection.jcl.ArrayList
import filters.{StrokeListener, StrokeFilter}
import ink.Ink
import ui.{SliderUi, ParameterUi}
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

  private val defaultValues = new DataSample()

  def createParameterUis( callback : ParameterUi => Unit ) {

    // TODO: Create parameter UI:s for all editable parameters.

    callback( new SliderUi( defaultValues, "maxRadius", 1, 50, this ) )

  }


}
