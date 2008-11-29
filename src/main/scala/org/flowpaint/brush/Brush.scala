package org.flowpaint.brush

import _root_.scala.collection.jcl.{HashSet, ArrayList}
import filters.{StrokeListener, StrokeFilter}
import ink.Ink
import ui.{BrushSliderUi, ParameterUi}

import util.DataSample

case class BrushProperty(name: String,
                        parameter: String,
                        default: Float,
                        min: Float,
                        max: Float,
                        editable: Boolean )


/**
 * Contains all settings for a certain brush tool.
 *
 * @author Hans Haggstrom
 */
case class Brush(inks: List[Ink], filters: List[StrokeFilter]) {

  private val defaultValues = new DataSample()
  private var brushProperties: List[BrushProperty] = Nil
  private var pixelProcessors: List[Ink] = inks
  private var strokeProcessors: List[StrokeFilter] = filters

  private val listeners = new HashSet[ChangeListener]()


  /**
   *  Applies the pixel processors of this brush on the specified pixel data,
   *  storing the effects of the pixel processors in the same pixel data.
   */
  def processPixel(pixel: DataSample) {
    pixelProcessors.foreach(_.processPixel(pixel))
  }


  /**
   *  Applies the stroke processors of this brush on the specified stroke point data.
   *  Calls the listener for each created final stroke point (may be zero also).
   */
  def processStrokePoint(pointData: DataSample, listener: StrokeListener) {

    if (!strokeProcessors.isEmpty) {

      strokeProcessors.head.filterStrokePoint(pointData, filters.tail, listener)
    }
  }

  def initializeStrokeStart(startPoint: DataSample) {
    startPoint.setValuesFrom(defaultValues)
  }


  def createParameterUis(callback: ParameterUi => Unit) {

    brushProperties.foreach((p: BrushProperty) => {

      if (p.editable) callback(
        new BrushSliderUi(
          defaultValues,
          p,
          this,
          notifyListeners))
    })

  }



  // Listener support
  type ChangeListener = (Brush) => Unit
  def addChangeListener(listener: ChangeListener) {listeners.add(listener)}
  def removeChangeListener(listener: ChangeListener) {listeners.remove(listener)}
  private def notifyListeners() {listeners foreach {listener => listener(this)}}

  // TODO: Some kind of listenable list might be handy.  Could do null-checks and such too..
  // Brush property management
  def getProperties() = brushProperties

  def addProperty(p: BrushProperty) {
    brushProperties = brushProperties ::: List(p)
    defaultValues.setProperty(p.parameter, p.default)
    notifyListeners()
  }

  def removeProperty(p: BrushProperty) {
    brushProperties = brushProperties.remove(_ == p)
    defaultValues.removeProperty(p.parameter)
    notifyListeners()
  }

  // Pixel processor management
  def getPixelProcessors() = pixelProcessors

  def addPixelProcessors(processor: Ink) {
    pixelProcessors = pixelProcessors ::: List(processor)
    notifyListeners()
  }

  def removePixelProcessor(processor: Ink) {
    pixelProcessors = pixelProcessors.remove(_ == processor)
    notifyListeners()
  }

  // Stroke processor management
  def getStrokeProcessors() = strokeProcessors

  def addStrokeProcessors(processor: StrokeFilter) {
    strokeProcessors = strokeProcessors ::: List(processor)
    notifyListeners()
  }

  def removeStrokeProcessor(processor: StrokeFilter) {
    strokeProcessors = strokeProcessors.remove(_ == processor)
    notifyListeners()
  }


}
