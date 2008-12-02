package org.flowpaint.brush


/**
 * A collection of, or view to, a set of Brushes.
 *
 * @author Hans Haggstrom
 */
trait BrushSet {

  /**
   * User readable name of this brush set.
   */
  def name : String

  /**
   * Returns the brushes in the set.
   */
  def getBrushes() : List[Brush]

  /**
   * Add a listener that is notified when the BrushSet changes.
   */
  def addChangeListener( listener : () => Unit )

  /**
   * Remove listener if found.
   */
  def removeChangeListener( listener : () => Unit )

}