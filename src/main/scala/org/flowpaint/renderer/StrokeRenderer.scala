package org.flowpaint.renderer

import brush.Brush
import java.awt.Color
import org.flowpaint.brush
import pixelprocessor.ScanlineCalculator
import property.{DataImpl, Data}
import util.MathUtils.squaredDistance
import util.{DataSample, RectangleInt, PropertyRegister}

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


    val startX = startData.getFloatProperty(PropertyRegister.PATH_X, 0)
    val startY = startData.getFloatProperty(PropertyRegister.PATH_Y, 0)
    val endX = endData.getFloatProperty(PropertyRegister.PATH_X, 0)
    val endY = endData.getFloatProperty(PropertyRegister.PATH_Y, 0)


    val x00 = startData.getFloatProperty(PropertyRegister.LEFT_EDGE_X, startX)
    val y00 = startData.getFloatProperty(PropertyRegister.LEFT_EDGE_Y, startY)
    val x01 = startData.getFloatProperty(PropertyRegister.RIGHT_EDGE_X, startX)
    val y01 = startData.getFloatProperty(PropertyRegister.RIGHT_EDGE_Y, startY)
    val x10 = endData.getFloatProperty(PropertyRegister.LEFT_EDGE_X, endX)
    val y10 = endData.getFloatProperty(PropertyRegister.LEFT_EDGE_Y, endY)
    val x11 = endData.getFloatProperty(PropertyRegister.RIGHT_EDGE_X, endX)
    val y11 = endData.getFloatProperty(PropertyRegister.RIGHT_EDGE_Y, endY)

    // Prepare data for corners

    val s00 = new DataImpl( startData )
    val s01 = new DataImpl( startData )
    val s10 = new DataImpl( endData )
    val s11 = new DataImpl( endData )

    s00.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, -1 )
    s01.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, 1 )
    s10.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, -1 )
    s11.setFloatProperty( PropertyRegister.POSITION_ACROSS_STROKE, 1 )

    s00.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s01.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s10.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )
    s11.setFloatProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )

    // Render triangles to surface, applying the pixel processing pipeline for each pixel

    val tempPixelData = new DataImpl()

    triangleRenderer.renderTriangle( surface.getWidth, surface.getHeight, x01, y01, x10, y10, x00, y00, s01, s10, s00, scanlineCalculator, surface  )
    triangleRenderer.renderTriangle( surface.getWidth, surface.getHeight, x10, y10, x11, y11, x01, y01, s10, s11, s01, scanlineCalculator, surface  )

 

  }




}