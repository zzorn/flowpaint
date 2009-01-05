package org.flowpaint.brush

import _root_.scala.collection.jcl.{HashSet, ArrayList}
import _root_.scala.xml.Node
import filters.{StrokeListener, StrokeFilter, PathProcessor}
import ink.Ink
import javax.swing.JComponent
import pixelprocessors.PixelProcessor
import property.{DataEditor, GradientSliderEditor, DataImpl, Data}
import ui.editors.Editor
import ui.slider.InkSliderUi
import ui.{BrushSliderUi, ParameterUi}
import util.{DataSample, ListenableList, Tome}
import org.flowpaint.util.ConfigurationMetadata


/**
 * Contains deserialization code for brushes.
 */
object Brush {
    def fromXML(node : Node) : Brush = {

      val name = (node \ "@id").text
      val settings = Data.fromXML( (node \ "settings").first )
      val pixelProcessorMetadatas = (node \ "pixelProcessors" \ "object") map
              {(n : Node) => ConfigurationMetadata.fromXML( n, classOf[Ink] )}
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
           pixelProcessorMetadatas: List[ConfigurationMetadata[Ink]],
           pathProcessorMetadatas: List[ConfigurationMetadata[PathProcessor]],
           initialEditors: List[ConfigurationMetadata[Editor]]) extends Tome {
  
    val settings = new DataImpl( initialSettings )
    val pixelProcessors = new ListenableList[ConfigurationMetadata[Ink]](pixelProcessorMetadatas, notifyListenersOnChildListChange)
    val strokeProcessors = new ListenableList[ConfigurationMetadata[PathProcessor]](pathProcessorMetadatas, notifyListenersOnChildListChange)
    val editors = new ListenableList[ConfigurationMetadata[Editor]](initialEditors, notifyListenersOnChildListChange)

    private val listeners = new HashSet[ChangeListener]()


    type ChangeListener = (Brush) => Unit

    private def notifyListenersOnChildListChange = (l: Any) => notifyListeners()

    private def notifyListeners() {listeners foreach {listener => listener(this)}}



    settings.addListener((data: Data, prop: String) => notifyListeners())

    def name = identifier

    def createPixelProcessors() : List[Ink] = {
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
