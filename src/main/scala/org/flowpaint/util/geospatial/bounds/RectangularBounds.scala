package org.flowpaint.util.geospatial

/**
 *
 *
 * @author Hans Haggstrom
 */
case class RectangularBounds(p1: Vector2, p2: Vector2) extends Bounds {
    def minX = Math.min(p1.x, p2.x)

    def minY = Math.min(p1.y, p2.y)

    def maxX = Math.max(p1.x, p2.x)

    def maxY = Math.max(p1.y, p2.y)


    def intersects(otherArea: Bounds): Boolean = otherArea match {
        case rect: RectangularBounds => (rect.minX < maxX && minX < rect.maxX) &&
                (rect.minY < maxY && minY < rect.maxY)

        case unknown: Any => throw new UnsupportedOperationException("Can not intersect " + this + " and " + unknown + ".")
    }


    override def toString: String = "RectangularBounds(" + p1 + ", " + p2 + ")"
}