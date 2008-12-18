package org.flowpaint.util.geospatial

/**
 * Contains a change that is applied to a Geometry with the specified ID, or a new Geometry that is inserted 
 *
 * @author Hans Haggstrom
 */
// TODO: add ID or matching function
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
     * The time starting from which this change took effect.
     */
    def time : Time


  
    // TODO: Better methods that allow changing settings for geometries as they are instantiated,
    // and that can create geometries that other changes can be applied to.
    // Probably needs further progress with the instantiatin stage before these can be formulated clearly

    /**
     * Applies the change before the specified geometry is applied, if needed.
     *
     * Return true if the specified geometry should be applied normally, false if it should be omitted.
     */
    def applyChangeBeforeAndKeep( tile : Tile, geometry : Geometry, geometryId : List[Geospatial.ID] ) : Boolean

    /**
     * Applies the change after the specified geometry has been applied, if needed.
     */
    def applyChangeAfter( tile : Tile, geometry : Geometry, geometryId : List[Geospatial.ID] )


}