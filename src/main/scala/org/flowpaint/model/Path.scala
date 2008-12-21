package org.flowpaint.model

import _root_.org.flowpaint.renderer.{StrokeRenderer, RenderSurface}
import _root_.org.flowpaint.util.DataSample
import brush.Brush
import filters.{StrokeListener, PathProcessor}
import ink.Ink
import property.{DataImpl, Data}
/**
 *  A path is a sequence of samples, forming a path on a 2D surface.  The Path can be connected to other paths.
 *  A path can be iterated in both directions, and it can be tested for overlap with a rectangular area.
 *
 * @author Hans Haggstrom
 */
class Path(brush: Brush) extends Renderable {

    val commonProperties = new DataImpl( brush.settings )

    private val pixelProcessors : List[Ink] = brush.createPixelProcessors()
    private val pathProcessors : List[PathProcessor]= brush.createPathProcessors()

    private var path: List[Data] = Nil

    def addPoint(data : Data) {

        // OPTIMIZE, can probably be done shorter with some list methods
        var points = List( data )
        pathProcessors.elements foreach ((p : PathProcessor) => points = p.handlePath( points ))
        

       path = path ::: points
    }


    def render(surface: RenderSurface) {

        if (path.isEmpty || path.tail.isEmpty ) return

        val segmentStartData: Data = new DataImpl(commonProperties)
        val segmentEndData: Data = new DataImpl( commonProperties )
        segmentEndData.setValuesFrom( path.head )

        var remainingPath = path.tail
        var previous = path.head

        while ( !remainingPath.isEmpty ) {
            val next = remainingPath.head

            // Remember the variable values along the line even if they are only present
            // in the points when they have changed from the previous value.
            segmentStartData.setValuesFrom(previous)
            segmentEndData.setValuesFrom(next)

            renderStrokeSegment(segmentStartData, segmentEndData, surface)

            previous = next
            remainingPath = remainingPath.tail
        }
    }

    private def renderStrokeSegment(startPoint: Data, endPoint: Data, surface: RenderSurface) {

        def processPixel( pixelData : Data ) {
          pixelProcessors foreach (_.processPixel(pixelData ))
        }

        val renderer = new StrokeRenderer()
        renderer.drawStrokeSegment(startPoint, endPoint, processPixel, surface)

    }

}

