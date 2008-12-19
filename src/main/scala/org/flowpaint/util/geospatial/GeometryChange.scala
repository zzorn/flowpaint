package org.flowpaint.util.geospatial

/**
 * Contains a change that is applied to a Geometry with the specified ID, or a new Geometry that is inserted 
 *
 * @author Hans Haggstrom
 */
trait GeometryChange {

    /**
     * The time starting from which this change took effect.
     */
    def time : Time

    /**
     * The ID of the changed geometry, as a sequence of ID:s starting from the RevisionedGeometry that stores the change.
     */
    def changedId() : GeometryId

    /**
     * Replaces the specified geometry that matched this change with the returned Geometry.  The returned Geometry can be an empty one,
     * or wrap the existing one and some additional ones, etc.
     */
    def change( changedGeometry : Geometry ) : Geometry


}