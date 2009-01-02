package org.flowpaint.util


import _root_.scala.xml.Elem
import property.{Data, DataImpl}

/**
 *  Common interface for pixel and path processors.
 */
trait Processor {
    private val settings: Data = new DataImpl()

    def init(initialSettings: Data) {
        settings.set(initialSettings)
        onInit()
    }

    protected def onInit() {}


    def getFloatProperty( propertyName : String, data : Data, defaultValue : Float ) : Float = data.getFloatProperty( propertyName, settings.getFloatProperty( propertyName, defaultValue ) )
    def getFloatProperty( propertyId : Int, data : Data, defaultValue : Float ) : Float = data.getFloatProperty( propertyId, settings.getFloatProperty( propertyId, defaultValue ) )
    def getStringProperty( propertyName : String, data : Data, defaultValue : String ) : String = data.getStringProperty( propertyName, settings.getStringProperty( propertyName, defaultValue ) )

    def getFloatProperty( propertyName : String, defaultValue : Float ) : Float = settings.getFloatProperty( propertyName, defaultValue )
    def getFloatProperty( propertyId : Int, defaultValue : Float ) : Float = settings.getFloatProperty( propertyId, defaultValue )
    def getStringProperty( propertyName : String, defaultValue : String ) : String = settings.getStringProperty( propertyName, defaultValue )

    def getSettings = settings

}



/**
 *  Common functionality of pixel and path processor metadata.
 *
 * @author Hans Haggstrom
 */
// TODO: Add support for defining UI widgets for modifying the settings
abstract class ProcessorMetadata[T <: Processor](processorType: Class[_ <: T], initialSettings : Data) {

    val settings = new DataImpl( initialSettings )

    def createProcessor(): T = {

        val processor: T = processorType.newInstance

        processor.init(settings)

        processor
    }

    def toXML() : Elem = <processor type={processorType.getName}>{settings.toXML()}</processor>

}

