package org.flowpaint.util.geospatial.shape

import property.Data

/**
 * Used for Triangle corners.  Contains a location, and optionally user specified content.
 * 
 * @author Hans Haggstrom
 */
case class Corner( location : Vector2, data : Data )