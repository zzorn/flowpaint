package org.flowpaint.model
import property.Data

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class PathPoint( data : Data,
               var path : Path,
               var forks : List[PathPoint],
               var next : PathPoint,
               var previous : PathPoint ) {




}