package org.flowpaint.util.geospatial

/**
 *  Immutable 2D vector.
 *
 * @author Hans Haggstrom
 */
case class Vector2(x: Unit, y: Unit) {
    type Unit = Geospatial.Unit


    override def toString: String = "Vector2(" + x + ", " + y + ")"
}