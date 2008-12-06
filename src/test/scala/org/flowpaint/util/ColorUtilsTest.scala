package org.flowpaint.util

import org.junit._
import Assert._

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@Test
class ColorUtilsTest {



  @Test
  def testHSLtoRGB {

    assertColorsEquals( (0, 0, 0), ColorUtils.HSLtoRGB( 0, 1, 0 ) )
    assertColorsEquals( (1, 1, 1), ColorUtils.HSLtoRGB( 0, 1, 1 ) )
    assertColorsEquals( (0.5f, 0.5f, 0.5f), ColorUtils.HSLtoRGB( 0, 0, 0.5f ) )
    assertColorsEquals( (1, 0, 0), ColorUtils.HSLtoRGB( 0, 1, 0.5f ) )
    assertColorsEquals( (0, 1, 0), ColorUtils.HSLtoRGB( 0.33333f, 1, 0.5f ) )
    assertColorsEquals( (0.5f, 0, 0), ColorUtils.HSLtoRGB( 0, 1, 0.25f ) )
    assertColorsEquals( (1f, 0.5f, 0.5f), ColorUtils.HSLtoRGB( 0, 1, 0.75f ) )

  }

  def assertColorsEquals( c1 : (Float,Float,Float), c2 :(Float, Float, Float) ) {

    assertEquals( c1._1, c2._1, 0.0001f )
    assertEquals( c1._2, c2._2, 0.0001f )
    assertEquals( c1._3, c2._3, 0.0001f )

  }
}