package org.flowpaint.brush


import _root_.scala.collection.jcl.HashSet

/**
 * A BrushSet backed up by a collection.
 *
 * Supports addition and removal, and may enforce a maximum size.
 *
 * @author Hans Haggstrom
 */
class FixedBrushSet(name_ : String, maxSize : Int ) extends BrushSet {

  private var brushes : List[Brush] = Nil
  private val listeners : HashSet[()=>Unit] = new HashSet()

  def this( name_ : String, maxSize : Int , initialBrushes : List[Brush] ) {
    this(name_, maxSize)
    brushes = initialBrushes
  }

  def this( name_ : String, initialBrushes : List[Brush] ) {
    this(name_, Math.MAX_INT)
    brushes = initialBrushes
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
   * Adds the specified brush first in the list of brushes.
   *
   * If there is any max size for the brush set, excess brushes are removed.
   */
  def addBrush( brush : Brush ) {

    brushes = brush :: brushes

    if (brushes.size > maxSize)
      {
        brushes = brushes.subseq( 0, maxSize ).toList
      }

    notifyListeners()
  }


}