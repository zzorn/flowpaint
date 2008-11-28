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

  /**
   * Interpolate this DataSample towards the specified target value, by the specified amount, 0 = no change, 1 = become target.
   * If a value is in only one of the samples, that value is used directly.
   */
  def interpolate( amount : Float, target: DataSample ) {

    val interpolateValuesProcedure = new TIntFloatProcedure {
      def execute(id: Int, value: Float): Boolean = {
        if (properties.contains( id )) {
          properties.put( id, MathUtils.interpolate(amount, properties.get(id), value) )
        }
        else {
          properties.put( id, value )
        }

        true
      }
    }


    target.properties.forEachEntry( interpolateValuesProcedure )
  }

  def containsId( id : Int ): Boolean = properties.contains(id)

  def getPropertyWithId(id: Int, defaultValue: Float): Float =
    if (properties.contains(id))
      properties.get(id)
    else
      defaultValue

  def setPropertyWithId(id: Int, value: Float) : Unit = properties.put( id, value )


  def removeProperty( name: String ) {
    val id = StrokePropertyRegister.getId( name )
    properties.remove( id )
  }


  // Use StrokePropertyRegister to allow querying by name instead of id:
  def contains( name : String ): Boolean = containsId( StrokePropertyRegister.getId( name ) )
  def getProperty(name: String, defaultValue: Float): Float = getPropertyWithId( StrokePropertyRegister.getId( name ), defaultValue )
  def setProperty(name: String, value: Float) : Unit= setPropertyWithId(  StrokePropertyRegister.getId( name ), value )

  override def toString: String = {

    var output = "DataSample( "

    properties.forEachEntry( new  TIntFloatProcedure (){
      def execute(id : Int, value: Float): Boolean = {

        val name = StrokePropertyRegister.getName( id )

        output += " "+name+" "+value + " "

        return true
      }

    })

    return output + ")"
  }
}