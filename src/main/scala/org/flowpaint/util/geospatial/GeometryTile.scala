package org.flowpaint.util.geospatial

/**
 * An area at a specific location, at a specific resolution.  Can contain extracted geometry.
 * Can listen to changes in the descriptions, and be updated when the descriptions change.
 * Updating can be done as a job in a worker queue (what's the Actor pattern for that in Scala?)
 * 
 * @author Hans Haggstrom
 */
trait GeometryTile {

    /**
     * The bounding area of this tile.
     */
    def bounds : Bounds

    /**
     * The smallest scale that is visible in this tile.  Anything smaller than this need not be added.
     */
    def minVisibleScale : Scale

    /**
     * Adds the specified shape to this tile.
     */
    def addShape( shape : Shape )

    /**
     * Removes the specified shape from this tile.
     */
    def removeShape( shapeId : Long )

}


