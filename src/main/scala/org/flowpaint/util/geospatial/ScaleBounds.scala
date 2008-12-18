package org.flowpaint.util.geospatial
/**
 * Contains a resolution and a bounds.
 *
 * Represents some object(s) that cover the specified area, and that have an individual size of the specified Scale. 
 *
 * @author Hans Haggstrom
 */
case class ScaleBounds( maxSize : Scale, bounds : Bounds  )