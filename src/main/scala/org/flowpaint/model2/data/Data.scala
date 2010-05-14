package org.flowpaint.model2

/**
 * Data node.
 * Used for parameters, 
 */
case class Data {
  var kind: Symbol = null
  var variables: Map[Symbol, Float] = Map
  var texts: Map[Symbol, String] = Map
  var nodes: Map[Symbol, Data] = Map
}

