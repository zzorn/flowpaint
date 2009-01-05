package org.flowpaint.util


import _root_.scala.xml.{Elem, Node}
import property.{Data, DataImpl}



/**
 *  Common interface for some class that needs configuration and xml serialization support.
 */
trait Configuration {
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
class ConfigurationMetadata[T <: Configuration](processorType: Class[_ <: T], initialSettings : Data) {

    val settings = new DataImpl( initialSettings )

    def createInstance(): T = {

        val instance: T = processorType.newInstance

        instance.init(settings)

        instance
    }

    def toXML() : Elem = <object type={processorType.getName}>{settings.toXML()}</object>


  override def hashCode: Int = (13 + processorType.hashCode) ^ settings.hashCode
}

/**
 * Deserialization
 */
object ConfigurationMetadata {

    def fromXML[T <: Configuration](node : Node, expectedType : Class[T]) : ConfigurationMetadata[T] = {

        val configuraionTypeName = (node \ "@type").text
        val settings = Data.fromXML( node )

      // Get class based on class name
      // TODO: Maybe check that the type is one of a set of allowed ones? (whitelist), to increase security.
      val configurationType = Class.forName(configuraionTypeName)

      if (! expectedType.isAssignableFrom( configurationType ) )
        throw new IllegalArgumentException( "Configuration should be of type "+expectedType.getName()+", " +
                "but a type of '"+configurationType+"' was requested " )

      new ConfigurationMetadata[T]( configurationType.asInstanceOf[Class[T]] , settings )
    }

}

