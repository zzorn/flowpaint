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
// TODO: Make it possible to pass Geometries or references to them as parameters to other Geometries, to enable modular, extensible construction of geometries
// TODO: Add a reference to named (library) objects to Data, so that it can contain references to (parametrizable) Geometries, Pixel Renderers, etc.. 
trait Geometry {

  /**
   * The bounding area for this Geometry.
   */
  def bounds : Bounds = null

  /**
   * The largest object size created by this Geometry (or larger).
   */
  def maxScale : Scale= null

  /**
   * The earliest existence of this Geometry.
   */
  def earliestExistence : Time= null

  /**
   * The last existence of this Geometry (can be infinite to indicate it exist up to present time).
   */
  def lastExistence : Time= null

  /**
   *  Applies this generator on the specified tile at the specified time in history.
   * Gets the changes to itself and its children as input too.
   */
  def instantiate( targetTile : Tile,
                 time : Time,
                 changes : ChangeSet )= null


}