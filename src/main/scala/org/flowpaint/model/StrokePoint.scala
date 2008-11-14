package org.flowpaint.model


import gnu.trove.TIntFloatHashMap

/**
 *  A datapoint along a stroke.  Can have one or more Float properties, indexed with Int id:s.
 *  Use StrokePropertyRegister to get or create the id of a given property name.
 *
 * @author Hans Haggstrom
 */
class StrokePoint {

  private val properties = new TIntFloatHashMap(2);

  def contains( id : Int ): Boolean = properties.contains(id)

  def getProperty(id: Int, defaultValue: Float): Float =
    if (properties.contains(id))
      properties.get(id)
    else
      defaultValue

  def setProperty(id: Int, value: Float) : Unit = properties.put( id, value )


  // Use StrokePropertyRegister to allow querying by name instead of id:
  def contains( name : String ): Boolean = contains( StrokePropertyRegister.getId( name ) )
  def getProperty(name: String, defaultValue: Float): Float = getProperty( StrokePropertyRegister.getId( name ), defaultValue )
  def setProperty(name: String, value: Float) : Unit= setProperty(  StrokePropertyRegister.getId( name ), value )


}