package org.flowpaint.util.geospatial
/**
 * Like Geometry, except it provides a global version history, and ability to rollback to a specific version.
 * 
 * @author Hans Haggstrom
 */
// TODO: Just provide the list of changes to contained geometries when instantiating?
// Maybe it could be part of the default geometry implementation.  Then we'd not need to keep long id strings necessarily.
// TODO: What about a global list of all changes though?
trait RevisionedGeometry 