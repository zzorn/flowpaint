package org.flowpaint.renderer

import _root_.org.flowpaint.property.Data
import brush.Brush
import java.awt.Color
import org.flowpaint.brush
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
  def drawStrokeSegment(startData: Data, endData: Data, brush: Brush, surface: RenderSurface) {


    val startX = startData.getFloatProperty(PropertyRegister.X, 0)
    val startY = startData.getFloatProperty(PropertyRegister.Y, 0)
    val endX = endData.getFloatProperty(PropertyRegister.X, 0)
    val endY = endData.getFloatProperty(PropertyRegister.Y, 0)

/*
    val DEFAULT_RADIUS = 10f
    val startAngle = startData.getProperty("angle", 0) / */
/* Math.Pi.toFloat * 2
    val startRadius = startData.getProperty("radius", DEFAULT_RADIUS)
    val endAngle = endData.getProperty("angle", 0) */
/* Math.Pi.toFloat * 2
    val endRadius = endData.getProperty("radius", DEFAULT_RADIUS)


    // Calculate corner points

    val startDeltaX = Math.cos(startAngle).toFloat * startRadius
    val startDeltaY = Math.sin(startAngle).toFloat * startRadius
    val endDeltaX = Math.cos(endAngle).toFloat * endRadius
    val endDeltaY = Math.sin(endAngle).toFloat * endRadius
*/

/*
    val x00 = startX - startDeltaX
    val y00 = startY - startDeltaY
    val x01 = startX + startDeltaX
    val y01 = startY + startDeltaY
    val x10 = endX - endDeltaX
    val y10 = endY - endDeltaY
    val x11 = endX + endDeltaX
    val y11 = endY + endDeltaY
*/

    val x00 = startData.getFloatProperty(PropertyRegister.LEFT_EDGE_X, startX)
    val y00 = startData.getFloatProperty(PropertyRegister.LEFT_EDGE_Y, startY)
    val x01 = startData.getFloatProperty(PropertyRegister.RIGHT_EDGE_X, startX)
    val y01 = startData.getFloatProperty(PropertyRegister.RIGHT_EDGE_Y, startY)
    val x10 = endData.getFloatProperty(PropertyRegister.LEFT_EDGE_X, endX)
    val y10 = endData.getFloatProperty(PropertyRegister.LEFT_EDGE_Y, endY)
    val x11 = endData.getFloatProperty(PropertyRegister.RIGHT_EDGE_X, endX)
    val y11 = endData.getFloatProperty(PropertyRegister.RIGHT_EDGE_Y, endY)

    // Prepare data for corners

    val s00 = new DataSample( startData )
    val s01 = new DataSample( startData )
    val s10 = new DataSample( endData )
    val s11 = new DataSample( endData )

    s00.setProperty( PropertyRegister.POSITION_ACROSS_STROKE, -1 )
    s01.setProperty( PropertyRegister.POSITION_ACROSS_STROKE, 1 )
    s10.setProperty( PropertyRegister.POSITION_ACROSS_STROKE, -1 )
    s11.setProperty( PropertyRegister.POSITION_ACROSS_STROKE, 1 )

    s00.setProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s01.setProperty( PropertyRegister.POSTION_ALONG_STROKE, 0 )
    s10.setProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )
    s11.setProperty( PropertyRegister.POSTION_ALONG_STROKE, 1 )

    // Render triangles to surface, applying the pixel processing pipeline for each pixel

    def pixelCallback(x : Int, y:Int, pixelData : DataSample ) {
      brush.processPixel( pixelData )
      surface.putPixel( x, y, pixelData )
    }

    triangleRenderer.renderTriangle( surface.width, surface.height, x01, y01, x10, y10, x00, y00, s01, s10, s00, pixelCallback )
    triangleRenderer.renderTriangle( surface.width, surface.height, x10, y10, x11, y11, x01, y01, s10, s11, s01, pixelCallback )


/*



    val squaredLength = squaredDistance(startX, startY, endX, endY)

    if (squaredLength > 0) {

      val length = Math.sqrt(squaredLength).toFloat

      // Used to store pixel specific properties in
      val pixelData = new DataSample()

      // Calculate a bounding box for the stroke segment
      val minX: Float = Math.min(startX - startRadius, endX - endRadius)
      val minY: Float = Math.min(startY - startRadius, endY - endRadius)
      val maxX: Float = Math.max(startX + startRadius, endX + endRadius)
      val maxY: Float = Math.max(startY + startRadius, endY + endRadius)

      // Calculate points one unit along in the direction of the start and end angles
      val startAngleOffsetX = Math.cos(startAngle).toFloat
      val startAngleOffsetY = Math.sin(startAngle).toFloat
      val startX2 = startX + startAngleOffsetX
      val startY2 = startY + startAngleOffsetY
      val endX2 = endX + Math.cos(endAngle).toFloat
      val endY2 = endY + Math.sin(endAngle).toFloat

      // Calculate the point at which the start and end angle converge, or wether the angles are parallel
      val centerPoint = Point(0, 0)
      val fixedCenterpoint = intersect(startX, startY, startX2, startY2,
        endX, endY, endX2, endY2, centerPoint)

      val strokePos = Point(0, 0)

      surface.provideContent(minX, minY, maxX, maxY, (x: Int, y: Int) => {
        // Default result color
        var color = TRANSPARENT_COLOR;

        // If the angles were parallel, we have to move the center point continuously relative to the query point
        if (!fixedCenterpoint) {
          centerPoint.x = x + startAngleOffsetX
          centerPoint.y = y + startAngleOffsetY
        }

        // Get the point along the stroke that this pixel maps to (depends on the local brush angle)
        val strokeIntersectionFound = intersect(x, y, centerPoint.x, centerPoint.y,
          startX, startY, endX, endY,
          strokePos)

        if (strokeIntersectionFound)
          {
            val startToStrokePosSquared = squaredDistance(startX, startY, strokePos.x, strokePos.y) / squaredLength
            val endToStrokePosSquared = squaredDistance(endX, endY, strokePos.x, strokePos.y) / squaredLength

            // Check that the current pixel maps to between the segment start and endpoint
            if (startToStrokePosSquared <= 1 && endToStrokePosSquared <= 1)
              {
                val positionAlongStroke = Math.sqrt(startToStrokePosSquared).toFloat

                val radius = util.MathUtils.interpolate(positionAlongStroke, startRadius, endRadius)
                val radiusSquared = radius * radius

                var centerDistanceSquared = squaredDistance(x, y, strokePos.x, strokePos.y)

                // Check that the current pixel is within the correct radius from the segment
                if (centerDistanceSquared <= radiusSquared && radius > 0)
                  {
                    val relativeCenterDistance = Math.sqrt(centerDistanceSquared).toFloat / radius

                    // Give across a sign depending on which side of the stroke the point is
                    val positionAcrossStroke = if (util.MathUtils.rightOf(startX, startY, endX, endY, x, y))
                      relativeCenterDistance
                    else
                      -relativeCenterDistance

                    pixelData.clear()

                    pixelData.setValuesFrom( startData )
                    pixelData.interpolate( positionAlongStroke, endData )
                    pixelData.setProperty( "positionAlongStroke", positionAlongStroke )
                    pixelData.setProperty( "positionAcrossStroke", positionAcrossStroke )

                    brush.processPixel( pixelData )

                    color = util.ColorUtils.createRGBAColor(
                      pixelData.getProperty("red",0),
                      pixelData.getProperty("green",0),
                      pixelData.getProperty("blue",0),
                      pixelData.getProperty("alpha",0))
                  }

              }

          }

        // Return calculated color
        color
      })


    }

*/

  }




}