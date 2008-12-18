package org.flowpaint.util.geospatial.geometry
/**
 * A Geometry that instantiates a MultiLine.
 *
 * Used e.g. for Strokes in Flowpaint.
 *
 * @author Hans Haggstrom
 */
// TODO: We need to be able to generate e.g. branching multilines from a multiline (would be nice if it was a seamless graph).
// for that, we could have a geometry that looks at an instantiated multiline, and modifies it (adds multilines)
// We could also have some kind of branching geometry that attaches child geometries of some sort along a multiline, and possibly at the end(s)
// -- that's actually very close to what the 3D plant generation system does -> could be used for that too.
// TODO: We probably need a graph / grid primitive instead or in addition to multilines.  It could easily express points, lines, surfaces/polygons, and complex volumes.
class MultiLineGeometry extends Geometry {


}

