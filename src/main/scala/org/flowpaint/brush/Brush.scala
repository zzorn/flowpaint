package org.flowpaint.brush

import _root_.scala.collection.jcl.{HashSet, ArrayList}
import filters.{StrokeListener, PathProcessorMetadata, StrokeFilter, PathProcessor}
import ink.{PixelProcessorMetadata, Ink}
import javax.swing.JComponent
import property.{DataEditor, GradientSliderEditor, DataImpl, Data}
import ui.slider.InkSliderUi
import ui.{BrushSliderUi, ParameterUi}
import util.{DataSample, ListenableList}

/*
case class BrushProperty(name: String,
                        parameter: String,
                        default: Float,
                        min: Float,
                        max: Float,
                        editable: Boolean,
                        gradient: Boolean)
*/


/**
 *   Contains all settings for a certain brush tool.
 *
 * @author Hans Haggstrom
 */
class Brush(val name: String,
           pixelProcessorMetadatas: List[PixelProcessorMetadata],
           pathProcessorMetadatas: List[PathProcessorMetadata],
           initialEditors: List[DataEditor]) {
  
    val settings = new DataImpl()
    val pixelProcessors = new ListenableList[PixelProcessorMetadata](pixelProcessorMetadatas, notifyListenersOnChildListChange)
    val strokeProcessors = new ListenableList[PathProcessorMetadata](pathProcessorMetadatas, notifyListenersOnChildListChange)
    val editors = new ListenableList[DataEditor](initialEditors, notifyListenersOnChildListChange)


    type ChangeListener = (Brush) => Unit

    private def notifyListenersOnChildListChange = (l: Any) => notifyListeners()

    private def notifyListeners() {listeners foreach {listener => listener(this)}}

    private val listeners = new HashSet[ChangeListener]()


    settings.addListener((data: Data, prop: String) => notifyListeners())

    /**
     *    Applies the pixel processors of this brush on the specified pixel data,
     *    storing the effects of the pixel processors in the same pixel data.
     */
    def processPixel(pixel: DataSample) {
        pixelProcessors.elements.foreach(_.processPixel(pixel))
    }


    /**
     *    Applies the stroke processors of this brush on the specified stroke point data.
     *    Calls the listener for each created final stroke point (may be zero also).
     */
    def processStrokePoint(pointData: DataSample, listener: StrokeListener) {

        if (!strokeProcessors.elements.isEmpty) {

            strokeProcessors.elements.head.filterStrokePoint(pointData, filters.tail, listener)
        }
    }

    def initializeStrokeStart(startPoint: DataSample) {
        settings.getFloatProperties(startPoint)
    }

  
    // Listener support
    def addChangeListener(listener: ChangeListener) {listeners.add(listener)}

    def removeChangeListener(listener: ChangeListener) {listeners.remove(listener)}


    /**
     *  Create a copy of this brush, that can be edited without affecting this Brush.
     *  NOTE: Assumes pixel processors and stroke processors and editors are immutable.
     */
    def createCopy(): Brush = {
        val copy = new Brush(name, pixelProcessors.elements, strokeProcessors.elements, editors.elements)
        copy.settings.set(settings)
        return copy
    }


    override def hashCode = {
        var code = name.hashCode

        code ^= 133 + settings.hashCode
        code ^= 234 + pixelProcessors.hashCode
        code ^= 435 + strokeProcessors.hashCode
        code ^= 978 + editors.hashCode

        code
    }
}
