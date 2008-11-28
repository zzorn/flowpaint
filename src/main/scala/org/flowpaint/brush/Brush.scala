package org.flowpaint.brush

import _root_.scala.collection.jcl.{HashSet, ArrayList}
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

  // Listener support
  type ChangeListener = (Brush ) => Unit
  private val listeners = new HashSet[ChangeListener]()
  def addChangeListener( listener: ChangeListener ) { listeners.add(listener) }
  def removeChangeListener( listener: ChangeListener ) { listeners.remove(listener) }
  private def notifyListeners() { listeners foreach {listener => listener( this )} }

  def initializeStrokeStart( startPoint : DataSample ){
    startPoint.setValuesFrom( defaultValues )
  }

  def filterStrokePoint(pointData:DataSample,listener: StrokeListener )  {

    if (!filters.isEmpty) {

      filters.head.filterStrokePoint( pointData, filters.tail,listener )
    }
  }

  private val defaultValues = new DataSample()

  def createParameterUis( callback : ParameterUi => Unit ) {

    // TODO: Create parameter UI:s for all editable parameters.

    callback( new SliderUi( defaultValues, "maxRadius", 1, 50, this, notifyListeners ) )

  }


}
