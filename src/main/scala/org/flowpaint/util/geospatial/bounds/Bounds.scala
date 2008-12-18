package org.flowpaint.util.geospatial
/**
 * Represents some area that can be queried for change, etc.
 * 
 * @author Hans Haggstrom
 */

trait Bounds  {

    /**
     * True if this Bounds intersects the other specified Bounds
     */
    def intersects( otherArea : Bounds ) : Boolean

}