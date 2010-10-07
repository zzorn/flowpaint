package org.flowpaint.renderer

import java.awt.Color
import org.flowpaint.brush.Brush
import org.flowpaint.brush
import org.flowpaint.pixelprocessor.ScanlineCalculator
import org.flowpaint.property.{DataImpl, Data}
import org.flowpaint.util.MathUtils.squaredDistance
import org.flowpaint.util.{DataSample, Rectangle, PropertyRegister}

/**
 *     Renders a stroke segment.
 *
 * @author Hans Haggstrom
 */
class StrokeRenderer {
  private val TRANSPARENT_COLOR = new Color(0, 0, 0, 0).getRGB()

  private val triangleRenderer = new TriangleRenderer()

  /**
   *     Renders a segment of a stroke.  The segment has start and end coordinates, radius, and angles.
   */
  def drawStrokeSegment(startData: Data, endData: Data, surface: RenderSurface, scanlineCalculator : ScanlineCalculator) {


    val flip = endData.getFloatProperty("FlipLeftRight", 0) > 0.5f

    val x0 = startData.getFloatProperty(PropertyRegister.PATH_X, 0)
    val y0 = startData.getFloatProperty(PropertyRegister.PATH_Y, 0)
    val x1 = endData.getFloatProperty(PropertyRegister.PATH_X, 0)
    val y1 = endData.getFloatProperty(PropertyRegister.PATH_Y, 0)


    val x00 = startData.getFloatProperty(PropertyRegister.LEFT_EDGE_X, x0)
    val y00 = startData.getFloatProperty(PropertyRegister.LEFT_EDGE_Y, y0)
    val x01 = startData.getFloatProperty(PropertyRegister.RIGHT_EDGE_X, x0)
    val y01 = startData.getFloatProperty(PropertyRegister.RIGHT_EDGE_Y, y0)
    var x10 = endData.getFloatProperty(PropertyRegister.LEFT_EDGE_X, x1)
    var y10 = endData.getFloatProperty(PropertyRegister.LEFT_EDGE_Y, y1)
    var x11 = endData.getFloatProperty(PropertyRegister.RIGHT_EDGE_X, x1)
    var y11 = endData.getFloatProperty(PropertyRegister.RIGHT_EDGE_Y, y1)

    // Prepare data for corners
    val s0 = new DataImpl( startData )
    val s1 = new DataImpl( endData )

    val s00 = new DataImpl( startData )
    val s01 = new DataImpl( startData )
    var s10 = new DataImpl( endData )
    var s11 = new DataImpl( endData )

    if (flip) {
        val t1 = x10; x10 = x11; x11 = t1 
        val t2 = y10; y10 = y11; y11 = t2
        val t3 = s10; s10 = s11; s11 = t3
    }


    s0.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, 0 )
    s1.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, 0 )
    s00.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, -1 )
    s01.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, 1 )
    s10.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, -1 )
    s11.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, 1 )

    s0.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s1.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )
    s00.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s01.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s10.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )
    s11.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )

    // Render triangles to surface, applying the pixel processing pipeline for each pixel

    val tempPixelData = new DataImpl()

    triangleRenderer.renderTriangle( surface.getWidth, surface.getHeight, x0, y0, x10, y10, x00, y00, s0, s10, s00, scanlineCalculator, surface  )
    triangleRenderer.renderTriangle( surface.getWidth, surface.getHeight, x10, y10, x1, y1, x0, y0, s10, s1, s0, scanlineCalculator, surface  )

    triangleRenderer.renderTriangle( surface.getWidth, surface.getHeight, x01, y01, x1, y1, x0, y0, s01, s1, s0, scanlineCalculator, surface  )
    triangleRenderer.renderTriangle( surface.getWidth, surface.getHeight, x1, y1, x11, y11, x01, y01, s1, s11, s01, scanlineCalculator, surface  )

  }




}