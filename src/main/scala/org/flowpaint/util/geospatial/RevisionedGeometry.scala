package org.flowpaint.util.geospatial
/**
 * Like Geometry, except it provides a global version history, and ability to rollback to a specific version.
 * 
 * @author Hans Haggstrom
 */
// TODO: Just provide the list of changes to contained geometries when instantiating?
// Maybe it could be part of the default geometry implementation.  Then we'd not need to keep long id strings necessarily.

// But we want to be able to directly re-use and duplicate geometries.. so a separate geometry that keeps the revision history
// could be useful, that way individual geometries stored in the library can be wrapped in one too

// Changes to a library geometry are applied first
// Changes should ideally be stored as close to the changed object as possible, but what to do when its duplicated?

// TODO: What about a global list of all changes though?
trait RevisionedGeometry extends Geometry {



}


