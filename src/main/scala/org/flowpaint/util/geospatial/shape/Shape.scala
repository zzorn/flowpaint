package org.flowpaint.util.geospatial
/**
 * A concrete shape of some kind (instantiated geometry).
 * 
 * @author Hans Haggstrom
 */
trait Shape {

  // TODO: Add the pixel processors to use for this shape, and maybe common settings / data

  // TODO: Maybe add intersection detection function

  /**
   * Render this shape to the fields of a tile using the pixel processors in this shape.
   */
  def render( tile : Tile )

}


