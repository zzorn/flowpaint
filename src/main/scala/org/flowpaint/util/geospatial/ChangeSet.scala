package org.flowpaint.util.geospatial
/**
 * Some set of changes to be applied to some geography.
 * 
 * @author Hans Haggstrom
 */

trait ChangeSet {

    /**
     * Returns the changes from this set of changes that apply to the specified child or its children.
     */
    def getChangeSetForChild( childId : Long, time : Time ) : ChangeSet

    /**
     * Applies the changes that are at the current level.  Returns modified, new or the unchanged Geometry.  
     */
    def applyChanges( geometry : Geometry, time : Time ) : Geometry


}

