package org.flowpaint.util.geospatial.shape

import org.flowpaint.util.geospatial.{Shape, Geospatial}

/**
 * A line that can have several corner points.  Also specifies a maximum radius that specifies the bounding area for the line.
 * 
 * @author Hans Haggstrom
 */
case class MultiLine( corners : List[Corner], maxRadius : Geospatial.Unit ) extends Shape