package org.flowpaint.util.geospatial
/**
 * A Geometry that consists of a sequence of child Geometries.
 *
 * Mutable, changes are communicated to listeners?
 *
 * @author Hans Haggstrom
 */
class CompositeGeometry() extends Geometry {

  val children = new ListenableList[Geometry]()


  def instantiate(targetTile: Tile, time: Time, parentIds: List[Geospatial.ID]) {

    // TODO: First check the bounding box, earliest time, and largest size.
    // The bounding box and other values could be variable, and this geometry could listen (or be notified by)
    // it's children, so that we always know when things change, and what area a geometry covers.
    // It makes the root objects a bit of a bottleneck on a server, but only when the bounds need to be updated
    // TODO: What about compacting bounds?  Requires a recalculation and complete iteration of contents? -> so only expand normally?  Sometimes contract as a maintenance task.

    // TODO: Add bounding check and bounding storage and listener support to the trait / superclass, as it is common for most / all.

    // TODO: Store the ID:s of the childs when they are added in a mapping, so that if we modify the
    // composite geometry and remove some children the ID:s of other children do not change.
    val childId : long = 0

    children foreach ( _.instantiate( targetTile, time, childId :: parentIds ) )    

  }


}