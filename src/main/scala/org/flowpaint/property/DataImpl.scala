package org.flowpaint.property

import _root_.scala.collection.mutable.{HashMap, HashSet, Map}
import util.{DataSample, PropertyRegister, Library}
/**
 *  Default implementation of Data.
 *
 * @author Hans Haggstrom
 */
final class DataImpl extends Data {
    protected val floatProperties = new DataSample()
    private val stringProperties = new HashMap[String, String]()
    private val listeners = new HashSet[Data#DataListener]

    def this( initialValues : Data ) {
      this()

      set( initialValues )
    }

    def addListener(listener: (Data, String) => Unit) {
        listeners.addEntry(listener)
    }

    def removeListener(listener: (Data, String) => Unit) {
        listeners.removeEntry(listener)
    }

    def getFloatProperty(name: String, default: Float): Float = floatProperties.getProperty(name, default)

    def getFloatProperty(id: Int, default: Float): Float = floatProperties.getProperty(id, default)

    def setFloatProperty(name: String, value: Float) = {
        floatProperties.setProperty(name, value)
        notifyListeners(name)
    }

    def setFloatProperty(id: Int, value: Float) = {
        floatProperties.setProperty(id, value)
        notifyListeners(PropertyRegister.getName(id))
    }

    def setStringProperty(name: String, value: String) = {
        stringProperties.put(name, value)
        notifyListeners(name)
    }

    def getStringProperty(name: String, default: String): String = {
        stringProperties.get(name) match {
            case None => default
            case Some(v) => v
        }
    }

    def getFloatProperties(target: DataSample) {
        target.setValuesFrom(floatProperties)
    }

    def setFloatProperties(values: DataSample) {
        floatProperties.setValuesFrom(values)
        notifyListeners()
    }


    def removeFloatProperty(name: String) {
        floatProperties.removeProperty(name)
        notifyListeners(name)
    }

    def removeStringProperty(name: String) {
        stringProperties.removeKey(name)
        notifyListeners(name)
    }

    def getStringProperties(target: Map[String, String]) {
        stringProperties foreach ((entry: (String, String)) => {target.put(entry._1, entry._2)})
    }

    def clear() {
        floatProperties.clear()
        stringProperties.clear()
        notifyListeners()
    }

    def set(sourceData: Data) {
        clear()
        setValuesFrom(sourceData)
    }


    def getReference[T]( name : String, default  : T, library : Library ) : T = {

        stringProperties.get(name) match {
            case None => default
            case Some(ref) => library.getTome( ref, default )
        }
    }

    def setReference( name : String, reference : String ) {
        stringProperties.put(name, reference)

        notifyListeners(name)
    }


    def setValuesFrom(sourceData: Data) = {
        sourceData.getFloatProperties(floatProperties)
        sourceData.getStringProperties(stringProperties)

      notifyListeners()
    }

    private def notifyListeners() {
        notifyListeners(null)
    }

    private def notifyListeners(changedProperty: String) {
        listeners foreach {(l: DataListener) => l(this, changedProperty)}
    }


    override def hashCode = {
        floatProperties.hashCode ^ stringProperties.hashCode
    }

  def interpolate( amount : Float, start: Data , target: Data ) {
    clear()
    setValuesFrom( start )
    interpolate(amount, target)
  }


    def interpolate( amount : Float, target: Data ) {
      floatProperties.interpolate(amount, (target.asInstanceOf[DataImpl]).floatProperties)

      // TODO: Interpolate string properties?
    }

}