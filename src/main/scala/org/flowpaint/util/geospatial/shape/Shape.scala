package org.flowpaint.util.geospatial
import property.Data

/**
 * A concrete shape of some kind (instantiated geometry).
 * 
 * @author Hans Haggstrom
 */
trait Shape {

  /**
   * Common settings for this Shape.
   */
  def settings : Data

    /**
     * The ID of the Geometry that generated this Shape. Can be used to make changes to the Shape through editing the Geometry.
     */
  def id : GeometryId

  // TODO: Add the pixel processors to use for this shape, and maybe common settings / data

  // TODO: Maybe add intersection detection function

  /**
   * Render this shape to the fields of a tile using the pixel processors in this shape.
   */
  def render( tile : Tile )

}


