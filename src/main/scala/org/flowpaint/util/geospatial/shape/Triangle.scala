package org.flowpaint.util.geospatial.shape

import org.flowpaint.util.geospatial.Shape

/**
 * A triangle shape.  Can contain user defined content for each corner.
 * 
 * @author Hans Haggstrom
 */

case class Triangle( a : Corner, b : Corner, c : Corner  ) extends Shape