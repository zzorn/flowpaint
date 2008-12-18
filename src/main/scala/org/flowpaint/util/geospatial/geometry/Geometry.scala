package org.flowpaint.util.geospatial
/**
 * Generates shapes on request for some tile with an area and min resolution.
 * 
 * Can be instantiated at some resolution and area in a tile.
 *
 * Possible to listen to changes in an area too.
 *
 * Uses a revision system to keep old versions and can extract any available old version (might discard some revisions though to reduce storage space).
 * It's also possible to listen to revisions, and be notified when they affect an area at some resolution (so that it needs an update).
 *
 * It is also possible to rollback to a specific revision.
 *
 * @author Hans Haggstrom
 */
// CHECK: Do revisions have to be global?  In the paint program we could keep a global list, but for spatial servers we could keep local changes only, to minimize bottlenecks.
trait Geometry {

  /**
   * Applies this generator on the specified tile at the specified time in history.
   * Gets passed in the id:s of all parents of itself, or Nil if it is the root Geometry. 
   */
  def instantiate( targetTile : Tile,
                 time : Time,
                 parentIds : List[Geospatial.ID],
                 changes : List[GeometryChange] )


}