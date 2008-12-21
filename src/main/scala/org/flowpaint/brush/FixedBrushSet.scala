package org.flowpaint.brush


import _root_.scala.collection.jcl.HashSet

/**
 * A BrushSet backed up by a collection.
 *
 * Supports addition and removal, and may enforce a maximum size.
 *
 * @author Hans Haggstrom
 */
class FixedBrushSet(name_ : String, maxSize : Int, initialBrushes : List[Brush] ) extends BrushSet {

  private var brushes : List[Brush] = initialBrushes.subseq(0, maxSize).toList
  private val listeners : HashSet[()=>Unit] = new HashSet()

  def this( name_ : String, maxSize : Int  ) {
    this(name_, maxSize, Nil)
  }

  def this( name_ : String, initialBrushes : List[Brush] ) {
    this(name_, Math.MAX_INT, initialBrushes)
  }

  def this( name_ : String ) {
    this(name_, Math.MAX_INT)
  }


  private def notifyListeners() {
    listeners foreach ((listener : ()=>Unit) => listener() ) 
  }

  def addChangeListener(listener: () => Unit) { listeners.add(listener) }

  def removeChangeListener(listener: () => Unit)  { listeners.remove(listener) }

  def name: String = name_

  def getBrushes(): List[Brush] = brushes

  /**
   * Adds the specified brush last in the list of brushes.
   *
   * If there is any max size for the brush set, excess brushes are removed first.
   */
  def addBrush( brush : Brush ) {

    if (brushes.size >= maxSize)
      {
        brushes = brushes.subseq( 0, maxSize-1 ).toList
      }

    brushes = brushes ::: List(brush)

    notifyListeners()
  }

  /**
   * Adds the specified brush first in the list of brushes, or moves it first if it is already added.
   *
   * If there is any max size for the brush set, excess brushes are removed.
   */
  def addOrMoveBrushFirst( brush : Brush ) {

    // Reove earlier brushes with same hashcode (= same settigns)
    val brushHash = brush.hashCode
    brushes = brushes.remove( (b:Brush)=> b.hashCode == brushHash  )
    
    brushes = brush :: brushes

    if (brushes.size > maxSize)
      {
        brushes = brushes.subseq( 0, maxSize ).toList
      }

    notifyListeners()
  }


}