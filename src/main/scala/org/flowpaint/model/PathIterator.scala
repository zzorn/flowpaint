package org.flowpaint.model
import org.flowpaint.property.Data

/**
 * Can be used to iterate a Path.  
 * 
 * @author Hans Haggstrom
 */
// TODO: Path iterator instead? go to next, previous point, follow a fork, get current data?
// TODO: MUtable version? - Path can have a new point added, which may have forking pahs.
// The added points glilicouet handled by a filter (PathPoint, addCallback(path,data), newCallback(data):PathPoint)
trait PathIterator {

  def getData( targetData : Data )

  def forks : List[PathIterator]

  /** Moves this iterator forward along the path, if possible */
  def next()

  /** Moves this iterator back along the path, if possible */
  def previous()

  def hasNext : Boolean

  def hasPrevious : Boolean

  /** The path that this iterator belongs to */
  def path : Path

}

