package org.flowpaint.property

import _root_.org.flowpaint.util.DataSample
import _root_.scala.collection.mutable.{HashMap, HashSet}

/**
 * Default implementation of Data.
 *
 * @author Hans Haggstrom
 */
final class DataImpl extends Data {

  private val floatProperties = new DataSample()
  private val stringProperties = new HashMap[ String, String ]()
  private val listeners = new HashSet[Data#DataListener]

  def addListener(listener: (Data, String) => Unit) {
    listeners.addEntry(listener)
  }

  def removeListener(listener: (Data, String) => Unit) {
    listeners.removeEntry( listener )
  }

  def getFloatProperty(name: String, default: Float): Float = floatProperties.getProperty( name, default )

  def setFloatProperty(name: String, value: Float) = {
    floatProperties.setProperty(name, value)
    notifyListeners(name)
  }

  def setStringProperty(name: String, value: String) = {
    stringProperties.put( name, value )
    notifyListeners(name)
  }

  def getStringProperty(name: String, default: String): String = {
    stringProperties.get(name) match {
      case None => default
      case Some(v) => v
    }
  }

  def getFloatProperties(target: DataSample) {
    target.setValuesFrom( floatProperties )
  }

  def setFloatProperties(values: DataSample) {
    floatProperties.setValuesFrom( values )
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

  def clear() {
    floatProperties.clear()
    stringProperties.clear()
    notifyListeners()
  }


  private def notifyListeners() {
    notifyListeners(null)
  }

  private def notifyListeners(changedProperty : String) {
    listeners foreach{ (l:DataListener) => l(this, changedProperty)}
  }
}