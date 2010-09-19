package org.flowpaint.model
import org.flowpaint.property.Data

/**
 * 
 * 
 * @author Hans Haggstrom
 */
case class PathPoint( data : Data,
               var path : Path,
               var forks : List[PathPoint],
               var next : PathPoint,
               var previous : PathPoint ) {




}