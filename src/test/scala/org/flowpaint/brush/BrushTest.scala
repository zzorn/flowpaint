package org.flowpaint.brush

import junit.Test
import org.junit._
import Assert._


/**
 * 
 *
 * @author Hans Haggstrom
 */
@Test
class BrushTest {


  @Test
  def testPropertyAdditionAndRemoval() {

    val brush = new Brush( Nil, Nil )

    assertEquals( Nil, brush.getProperties )

    val p1 = new BrushProperty("Foo", "foo", 0, 0, 1, true, false)
    val p2 = new BrushProperty("Bar", "bar", 0, 0, 1, true, false)
    val p3 = new BrushProperty("Zar", "zar", 0, 0, 1, true, false)
    brush.addProperty( p1 )

    assertEquals( List(p1), brush.getProperties )

    brush.addProperty( p2 )
    brush.addProperty( p3 )

    assertEquals( List(p1, p2, p3), brush.getProperties )

    brush.removeProperty( p2 )

    assertEquals( List(p1, p3), brush.getProperties )

    brush.removeProperty( p1 )
    brush.removeProperty( p3 )

    assertEquals( Nil, brush.getProperties )
  }

}