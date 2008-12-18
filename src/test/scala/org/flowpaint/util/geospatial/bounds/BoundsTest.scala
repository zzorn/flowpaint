package org.flowpaint.util.geospatial.bounds


import org.junit._
import Assert._


/**
 *
 *
 * @author Hans Haggstrom
 */
@Test
class BoundsTest {

    @Test
    def testRectangle {

        intersectTester( true, 0,0, 1,1, 0,0, 1,1 )
        intersectTester( false, 1,1, 2,2, 0,0, 1,1 )
        intersectTester( true, 1,1, 2,2, 0,0, 1.001,1.001 )

        intersectTester( false, 0,0, 0,0, 0,0, 0,0 )
        intersectTester( false, 0,0, 0,0, 1,1, 1,1 )

        intersectTester( true, 0,0, 0,0, -1,-1, 1,1 )


        intersectTester( true, 0,0, 2,2, 1,1, 3,3 )

        intersectTester( true, 0.001,0, 2,2, 1,1, 3,3 )

        intersectTester( true, 0,0, 2,1, 1,0, 2,1 )
    }

    def intersectTester(expected: Boolean,
                       x1: Vector2.Unit, y1: Vector2.Unit, x2: Vector2.Unit, y2: Vector2.Unit,
                       x3: Vector2.Unit, y3: Vector2.Unit, x4: Vector2.Unit, y4: Vector2.Unit) {

        val r1 = RectangularBounds(Vector2(x1, y1), Vector2(x2, y2))
        val r2 = RectangularBounds(Vector2(x3, y3), Vector2(x4, y4))

        assertEquals("The bounding areas " + r1 + " and " + r2 + " should " + (if (!expected) "not " else "") + "intersect!",
            expected, r1 intersects r2);

    }

}