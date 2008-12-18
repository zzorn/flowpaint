package org.flowpaint.util.geospatial.geometry
/**
 * Adds zero or more of several alternative Geometries (or zero or one?), based on the value of an expression,
 * which can depend on field variables at the location of the geometry, and presence of shapes and their
 * settings for the location of the geometry.  Can also depend on query properties like time,
 * and the settings of this geometry that it inherits from its parents.
 *
 * @author Hans Haggstrom
 */
// TODO: What's the most optimal / best approach?  Simple present or not query, or a more rich switch query,
// or a sequence of include or not queries?  Maybe a switch or sequence, to raise the level of the basic language elements..  
class ConditionalGeometry extends Geometry {

}