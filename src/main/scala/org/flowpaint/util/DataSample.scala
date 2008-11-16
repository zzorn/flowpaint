package org.flowpaint.util

import gnu.trove.{TIntFloatHashMap, TIntFloatProcedure}

/**
 *  A datapoint along a stroke.  Can have one or more Float properties, indexed with Int id:s.
 *  Use StrokePropertyRegister to get or create the id of a given property name.
 *
 * @author Hans Haggstrom
 */
class DataSample {

  def this( source : DataSample ) = {
    this()
    setValuesFrom( source )
  }

  private val properties = new TIntFloatHashMap(2)

  private val copyValuesProcedure = new TIntFloatProcedure {
    def execute(id: Int, value: Float): Boolean = {
      setPropertyWithId(id, value)
      true
    }
  }

  def setValuesFrom( otherSample : DataSample ) {
    otherSample.properties.forEachEntry( copyValuesProcedure)
  }

  /**
   * Removes all current values
   */
  def clear(){
    properties.clear
  }

  def containsId( id : Int ): Boolean = properties.contains(id)

  def getPropertyWithId(id: Int, defaultValue: Float): Float =
    if (properties.contains(id))
      properties.get(id)
    else
      defaultValue

  def setPropertyWithId(id: Int, value: Float) : Unit = properties.put( id, value )


  // Use StrokePropertyRegister to allow querying by name instead of id:
  def contains( name : String ): Boolean = containsId( StrokePropertyRegister.getId( name ) )
  def getProperty(name: String, defaultValue: Float): Float = getPropertyWithId( StrokePropertyRegister.getId( name ), defaultValue )
  def setProperty(name: String, value: Float) : Unit= setPropertyWithId(  StrokePropertyRegister.getId( name ), value )


}