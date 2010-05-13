package org.flowpaint.util

/**
 * A map that allows listening to changes.
 * 
 * @author Hans Haggstrom
 */

object CallbackMap {

  def apply[K,V]( initialMap : Map[K,V], onAdded : (K,V) => Unit, onRemoved : (K,V) => Unit ) = new CallbackMap( initialMap, onAdded, onRemoved, null )
  def apply[K,V]( initialMap : Map[K,V], onAdded : (K,V) => Unit ) = new CallbackMap( initialMap, onAdded, null, null )
  def apply[K,V]( initialMap : Map[K,V] ) = new CallbackMap( initialMap, null, null, null )
  def apply[K,V]() = new CallbackMap( null, null, null, null )

  implicit def callbackMap2Map[K,V](callbackMap : CallbackMap[K,V]) : Map[K,V] = callbackMap.value
}

class CallbackMap[K,V] ( initialMap : Map[K,V], onAdded : (K,V) => Unit, onRemoved : (K,V) => Unit, canAdd : (K,V) => Boolean) extends PartialFunction[K, V] with Collection[(K, V)]  {

  private var _map : Map[K,V] = if (initialMap == null) Map() else initialMap

  def value : Map[K,V] = _map

  def elements = _map.elements
  def size = _map.size
  def isDefinedAt(x: K) = _map.isDefinedAt(x)
  def apply(t: K) = _map.apply( t )


  def clear() {
    _map foreach { case (k, v) => onRemoved( k, v ) }
    _map = Map()
  }

  def put( key : K, value : V ) {
    if (canAdd != null) if (!canAdd(key, value)) throw new IllegalArgumentException( "Can not add a new entry with key '"+key+"' and value '"+value+"'" )

    val entry = (key, value)
    _map = _map + entry
    if (onAdded != null) onAdded( key, value )
  }

  def remove( key : K ) {
    if (has( key )) {
      val oldValue = _map( key )
      _map = _map - key
      if (onRemoved != null) onRemoved( key, oldValue )
    }
  }

  def putAll( elements : Iterator[(K,V)] ) = elements foreach { case (key, value) => put( key, value) }
  def removeAll( elements : Iterator[K] ) = elements foreach remove

  def has( key : K ) = _map.contains( key )
  def contains( key : K ) = has( key )
}
