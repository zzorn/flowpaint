package org.flowpaint.util.geospatial

/**
 * Contains an atomic change to some area of a Geometry.
 *
 * @author Hans Haggstrom
 */

trait GeometryChange {

    /**
     * The area that the change affects.
     */
    def bounds: Bounds

    /**
     * The scale of the largest object in this change.
     */
    def maxScale: Scale

    /**
     * Applies this change to the specified tile.
     */
    def applyTo( tile : GeometryTile )

}