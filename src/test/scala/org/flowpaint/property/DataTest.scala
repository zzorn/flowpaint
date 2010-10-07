package org.flowpaint.property
import org.junit._
import Assert._

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@Test
class DataTest {

  @Test
  def testInterpolate(){

      val epsilon = 0.0001f

      val a = new DataImpl( ("foo", 4f), ("bar", 1f) )
      val b = new DataImpl( ("foo", 2f), ("baz", 3f) )
      a.setStringProperty( "sound", "nya" )
      b.setStringProperty( "sound", "arrr" )

      val c = new DataImpl(a)
      c.interpolate(0.5f, b)

      val d = new DataImpl()
      d.interpolate(0.5f, a, b)

      val e = new DataImpl(a)
      e.interpolate(0.75f, b)

      assertEquals( 3f, c.getFloatProperty( "foo",-1 ), epsilon )
      assertEquals( 1f, c.getFloatProperty( "bar",-1 ), epsilon )
      assertEquals( 3f, c.getFloatProperty( "baz",-1 ), epsilon )

      assertEquals( 3f, d.getFloatProperty( "foo",-1 ), epsilon )
      assertEquals( 1f, d.getFloatProperty( "bar",-1 ), epsilon )
      assertEquals( 3f, d.getFloatProperty( "baz",-1 ), epsilon )

      assertEquals( "nya", c.getStringProperty( "sound","" ) )
      assertEquals( "arrr", e.getStringProperty( "sound","" ) )
  }

}