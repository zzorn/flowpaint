package org.flowpaint.brush


import scala.xml.Node
import org.flowpaint.util.Tome
import org.flowpaint.FlowPaint

/**
 * A collection of, or view to, a set of Brushes.
 *
 * @author Hans Haggstrom
 */
trait BrushSet extends Tome {

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



object BrushSet {

  def fromXML(node : Node) : BrushSet = {
    
    val id = (node \ "@id").text
    val name = (node \ "@name").text
    val refs = (node \ "brushref") map {(n:Node) => n.text}

    var brushes : List[Brush] = (refs map {(s:String) => FlowPaint.library.getTome( s, null.asInstanceOf[Brush] ) }).toList
    brushes = brushes.remove( _ == null )

    new FixedBrushSet(id, name, brushes )
  }

}