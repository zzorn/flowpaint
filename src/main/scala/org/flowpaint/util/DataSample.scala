package org.flowpaint.util

import gnu.trove.{TFloatFunction, TIntFloatHashMap, TIntFloatProcedure}

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

  def this( values: (String, Float)* ) = {
    this()

    values.foreach( (v : (String, Float)) => {setProperty(v._1, v._2)})    
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

  /**
   * Divide all values in this data sample with the specified number.
   */
  def /= ( divisor : Float ) {

    if (divisor == 0 ) throw new IllegalArgumentException( "Can not divide with zero")

    properties.transformValues( new TFloatFunction() {
      def execute (v : Float) : Float = {
        v / divisor
      }
    })
  }

  /**
   * Multiply all values in this data sample with the specified number.
   */
  def *= ( multiplicand : Float ) {

    properties.transformValues( new TFloatFunction() {
      def execute (v : Float) : Float = {
        v * multiplicand
      }
    })
  }

  /**
   * Add values of other DataSample to the values of this one.
   */
  def += ( other : DataSample ) {
    if (other == null ) throw new IllegalArgumentException( "Can not add null")

    other.properties.forEachEntry( new  TIntFloatProcedure (){
      def execute(id : Int, otherValue: Float): Boolean = {

        val myValue = getPropertyWithId( id, 0 )

        properties.put( id, myValue + otherValue )

        return true
      }

    })
  }

  /**
   * Subtract values of other DataSample from the values of this one.
   */
  def -= ( other : DataSample ) {
    if (other == null ) throw new IllegalArgumentException( "Can not subtract null")

    other.properties.forEachEntry( new  TIntFloatProcedure (){
      def execute(id : Int, otherValue: Float): Boolean = {

        val myValue = getPropertyWithId( id, 0 )

        properties.put( id, myValue - otherValue )

        return true
      }

    })
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


  override def hashCode = {
    properties.hashCode
  }
}