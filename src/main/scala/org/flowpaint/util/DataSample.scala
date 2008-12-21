package org.flowpaint.util

import _root_.scala.xml.Elem
import gnu.trove.{TFloatFunction, TIntFloatHashMap, TIntFloatProcedure}

/**
 *  A datapoint along a stroke.  Can have one or more Float properties, indexed with Int id:s.
 *  Use PropertyRegister to get or create the id of a given property name.
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


   private class InterpolateProcedure() extends TIntFloatProcedure {
     def setAmount(interpolationAmount : Float) {
       amount = interpolationAmount
       oneMinusAmount = 1f - amount
     }

     private var amount : Float = 0
     private var oneMinusAmount : Float = 1

      def execute(id: Int, value: Float): Boolean = {
        if (properties.contains( id )) {
          properties.put( id, oneMinusAmount * properties.get(id) + amount * value )
        }
        else {
          properties.put( id, value )
        }

        true
      }
    }

  private val properties = new TIntFloatHashMap(2)

  // OPTIMIZE: These use one some space both for each data sample.  
  // But they can't be static as we work with data samples from several threads.. Could they be made thread local?
  private val interpolateProcedure = new InterpolateProcedure()
  private val copyValuesProcedure = new TIntFloatProcedure {
    def execute(id: Int, value: Float): Boolean = {
      setProperty(id, value)
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
    interpolateProcedure.setAmount( amount )
    target.properties.forEachEntry( interpolateProcedure )
  }

  /**
   * Clears this sample, sets it to start, and interpolates it towards the target by the specified amount, 0 = start value, 1 = target value.
   * If a value is in only one of the samples, that value is used directly.
   */
  def interpolate( amount : Float, start: DataSample , target: DataSample ) {
    clear()
    setValuesFrom( start )
    interpolate(amount, target)
  }

  def contains( id : Int ): Boolean = properties.contains(id)

  def getProperty(id: Int, defaultValue: Float): Float =
    if (properties.contains(id))
      properties.get(id)
    else
      defaultValue

  def setProperty(id: Int, value: Float) : Unit = properties.put( id, value )


  def removeProperty( name: String ) {
    val id = PropertyRegister.getId( name )
    properties.remove( id )
  }

/* The implementation of these might be broken in some way.  If they are needed later, unit test them properly.
  */
/**
   * Divide all values in this data sample with the specified number.
   */
/*
  def /= ( divisor : Float ) {

    if (divisor == 0 ) throw new IllegalArgumentException( "Can not divide with zero")

    properties.transformValues( new TFloatFunction() {
      def execute (v : Float) : Float = {
        v / divisor
      }
    })
  }

  */
/**
   * Multiply all values in this data sample with the specified number.
   */
/*
  def *= ( multiplicand : Float ) {

    properties.transformValues( new TFloatFunction() {
      def execute (v : Float) : Float = {
        v * multiplicand
      }
    })
  }

  */
/**
   * Add values of other DataSample to the values of this one.
   */
/*
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

  */
/**
   * Subtract values of other DataSample from the values of this one.
   */
/*
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
*/


  // Use PropertyRegister to allow querying by name instead of id:
  def contains( name : String ): Boolean = contains( PropertyRegister.getId( name ) )
  def getProperty(name: String, defaultValue: Float): Float = getProperty( PropertyRegister.getId( name ), defaultValue )
  def setProperty(name: String, value: Float) : Unit= setProperty(  PropertyRegister.getId( name ), value )

  override def toString: String = {

    var output = "DataSample( "

    properties.forEachEntry( new  TIntFloatProcedure (){
      def execute(id : Int, value: Float): Boolean = {

        val name = PropertyRegister.getName( id )

        output += " "+name+" "+value + " "

        return true
      }

    })

    return output + ")"
  }


  override def hashCode = {
    properties.hashCode
  }


  def toTupleList() : List[Tuple2[String,Float]] = {
      var elements : List[Tuple2[String,Float]] = Nil

           properties.forEachEntry( new  TIntFloatProcedure (){
             def execute(id : Int, value: Float): Boolean = {

               val name = PropertyRegister.getName( id )

               elements = (name, value) :: elements

               return true
             }

           })

    elements
  }

    def toXML() : List[Elem] = {
        toTupleList() map ( (e : (String,Float )) => <text name={e._1}>{e._2}</text>  )
    }

}