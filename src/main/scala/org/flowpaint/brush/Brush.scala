package org.flowpaint.brush

import _root_.scala.collection.jcl.{HashSet, ArrayList}
import filters.{StrokeListener, StrokeFilter}
import ink.Ink
import ui.{SliderUi, ParameterUi}
import util.DataSample

case class BrushProperty(name :String, parameter: String, default: Float, min: Float, max: Float, editable: Boolean)

/**
 *
 *
 * @author Hans Haggstrom
 */
case class Brush(ink: Ink, filters: List[StrokeFilter]) {
  private val defaultValues = new DataSample()
  private var brushProperties: List[BrushProperty] = Nil
  private val listeners = new HashSet[ChangeListener]()

  // Listener support
  type ChangeListener = (Brush) => Unit

  def addChangeListener(listener: ChangeListener) {listeners.add(listener)}

  def removeChangeListener(listener: ChangeListener) {listeners.remove(listener)}

  private def notifyListeners() {listeners foreach {listener => listener(this)}}


  def getProperties() = brushProperties

  def addProperty(p: BrushProperty) {
    brushProperties = p :: brushProperties
    defaultValues.setProperty(p.parameter, p.default)
    notifyListeners()
  }

  def removeProperty(p: BrushProperty) {
    brushProperties = brushProperties.remove(_ == p)
    defaultValues.removeProperty(p.parameter)
    notifyListeners()
  }


  def initializeStrokeStart(startPoint: DataSample) {
    startPoint.setValuesFrom(defaultValues)
  }

  def filterStrokePoint(pointData: DataSample, listener: StrokeListener) {

    if (!filters.isEmpty) {

      filters.head.filterStrokePoint(pointData, filters.tail, listener)
    }
  }


  def createParameterUis(callback: ParameterUi => Unit) {

    brushProperties.foreach((p: BrushProperty) => {

      if (p.editable) callback(
        new SliderUi(
          defaultValues,
          p,
          this,
          notifyListeners))
    })

  }


}
