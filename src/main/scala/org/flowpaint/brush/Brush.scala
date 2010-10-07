package org.flowpaint.brush

import scala.xml.Node
import org.flowpaint.filters.{StrokeListener, StrokeFilter, PathProcessor}
import org.flowpaint.ink.Ink
import javax.swing.JComponent
import org.flowpaint.pixelprocessor.{PixelProcessor, ScanlineCalculator}
import org.flowpaint.property.{DataEditor, GradientSliderEditor, DataImpl, Data}
import org.flowpaint.ui.editors.Editor
import org.flowpaint.ui.slider.InkSliderUi
import org.flowpaint.ui.{BrushSliderUi, ParameterUi}
import org.flowpaint.util.{DataSample, ListenableList, Tome}
import org.flowpaint.util.ConfigurationMetadata
import java.util.HashSet
import scala.collection.JavaConversions._

/**
 * Contains deserialization code for brushes.
 */
object Brush {
    def fromXML(node : Node) : Brush = {

      val name = (node \ "@id").text
      val settings = Data.fromXML( (node \ "settings").first )
      val pixelProcessorMetadatas = (node \ "pixelProcessors" \ "object") map
              {(n : Node) => ConfigurationMetadata.fromXML( n, classOf[PixelProcessor] )}
      val pathProcessorMetadatas = (node \ "pathProcessors" \ "object") map
                {(n : Node) => ConfigurationMetadata.fromXML( n, classOf[PathProcessor] )}
      val editorMetadatas = (node \ "editors" \ "object") map
                {(n : Node) => ConfigurationMetadata.fromXML( n, classOf[Editor] )}

      val brush = new Brush(name, settings, pixelProcessorMetadatas.toList, pathProcessorMetadatas.toList, editorMetadatas.toList )

      brush
    }
}


/**
 *   Contains all settings for a certain brush tool.
 *
 * @author Hans Haggstrom
 */
class Brush(val identifier: String,
            initialSettings : Data,
           pixelProcessorMetadatas: List[ConfigurationMetadata[PixelProcessor]],
           pathProcessorMetadatas: List[ConfigurationMetadata[PathProcessor]],
           initialEditors: List[ConfigurationMetadata[Editor]]) extends Tome {
  
    val settings = new DataImpl( initialSettings )
    val pixelProcessors = new ListenableList[ConfigurationMetadata[PixelProcessor]](pixelProcessorMetadatas, notifyListenersOnChildListChange)
    val strokeProcessors = new ListenableList[ConfigurationMetadata[PathProcessor]](pathProcessorMetadatas, notifyListenersOnChildListChange)
    val editors = new ListenableList[ConfigurationMetadata[Editor]](initialEditors, notifyListenersOnChildListChange)

    private val listeners = new HashSet[ChangeListener]()

    private var scanlineCalculator : ScanlineCalculator = null


    type ChangeListener = (Brush) => Unit

    private def notifyListenersOnChildListChange = (l: Any) => notifyListeners()

    private def notifyListeners() {
      scanlineCalculator = null; // Reset
      listeners foreach {listener => listener(this)}
    }

    settings.addListener((data: Data, prop: String) => notifyListeners())

    def getScanlineCalculator = {
      if (scanlineCalculator == null) {
        scanlineCalculator = new ScanlineCalculator()
        scanlineCalculator.init( createPixelProcessors(), settings )
      }

      scanlineCalculator
    }

    def name = identifier

    def createPixelProcessors() : List[PixelProcessor] = {
        pixelProcessors.elements.map( _.createInstance() )
    }

    def createPathProcessors() : List[PathProcessor] = {
        strokeProcessors.elements.map( _.createInstance() )
    }

    def createEditors() : List[Editor] = {
        val list = editors.elements.map( _.createInstance() )
        list foreach ( _.setEditedData( settings ) )
        list
    }

    // Listener support
    def addChangeListener(listener: ChangeListener) {listeners.add(listener)}

    def removeChangeListener(listener: ChangeListener) {listeners.remove(listener)}


    /**
     *  Create a copy of this brush, that can be edited without affecting this Brush.
     *  NOTE: Assumes pixel processors and stroke processors and editors are immutable.
     */
    def createCopy(): Brush = new Brush(identifier, settings, pixelProcessors.elements, strokeProcessors.elements, editors.elements)


    override def hashCode = {
        var code = identifier.hashCode

        code ^= 133 + settings.hashCode
        code ^= 234 + pixelProcessors.hashCode
        code ^= 435 + strokeProcessors.hashCode
        code ^= 978 + editors.hashCode

        code
    }


    def toXML() = <brush id={identifier} description={name}>
                    <settings>{settings.toXML()}</settings>
                    <pathProcessors>{strokeProcessors.elements map (_.toXML()) }</pathProcessors>
                    <pixelProcessors>{pixelProcessors.elements map (_.toXML()) }</pixelProcessors>
                    <editors>{editors.elements map (_.toXML()) }</editors>
                  </brush>
}
