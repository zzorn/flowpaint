package org.flowpaint.renderer


import junit.framework.TestCase
import org.junit._
import Assert._
import java.awt.Color

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class SurfaceTest extends TestCase {

  def testSingleRenderSurface {
    assertSurfaceWorks( new SingleRenderSurface(createTestProvider, 8) )
  }

/*
  def testPackedSurface {
    assertSurfaceWorks( new PackedSurface(createTestProvider) )
  }
*/

  def createTestProvider = new PictureProvider {
      def updateSurface(surface: RenderSurface) {
        surface.clearToColor( Color.RED )
      }
    }

  def assertSurfaceWorks( surface : RenderSurface ) {
    surface.setViewPortSize( 240, 300 )

    assertPixelIsColor( surface, 10, 10, Color.RED )
  }

  def assertPixelIsColor(surface : RenderSurface, x : Int, y : Int, expectedColor : Color) {
    val pixel : Int = surface.getPixel( 10, 10 )
    assertEquals( expectedColor.getRGB, pixel )
  }

}

