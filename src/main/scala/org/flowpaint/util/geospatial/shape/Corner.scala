package org.flowpaint.util.geospatial.shape

import org.flowpaint.property.Data
import org.flowpaint.util.geospatial.Vector2

/**
 * Used for Triangle corners.  Contains a location, and optionally user specified content.
 *
 *
 * 
 * @author Hans Haggstrom
 */
// TODO: The location could be retrieved from within the data?  Allows more uniform manipulation of it..
case class Corner( location : Vector2, data : Data )