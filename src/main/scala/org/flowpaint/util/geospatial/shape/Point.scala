package org.flowpaint.util.geospatial

import shape.Corner

/**
 * A point object with some specified radius (which gives the scale and bounding area too).
 * Also can contain user defined content.
 * 
 * @author Hans Haggstrom
 */
class Point( corner : Corner, radius : Geospatial.Unit ) extends Shape

