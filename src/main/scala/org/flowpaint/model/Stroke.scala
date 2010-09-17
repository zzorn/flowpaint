package org.flowpaint.model


import org.flowpaint.brush.Brush
import org.flowpaint.filters.StrokeListener
import org.flowpaint.property.Data
import org.flowpaint.renderer.{PictureProvider, RenderSurface}
import org.flowpaint.renderer.StrokeRenderer
import org.flowpaint.util.{DataSample, Rectangle}

/**
 *      A brush stroke on some layer.
 *
 * @author Hans Haggstrom
 */
case class Stroke(brush: Brush) extends PictureProvider {

    private var paths: List[Path] = Nil

    def addInputPoint( pointData : Data, surface: RenderSurface ) {

      if (paths isEmpty){
        // TODO: Create the initial paths
        // IDEA: We could have separate filters that check if a new path should be started (for each filtered path point added)
        // Also run them once for the first stroke input
        addPath( brush )
      }

      paths foreach ( _.addPoint( pointData, surface ) )
    }

    def addPath(brush: Brush): Path = {
        val path = new Path(brush)

        addPath(path)

        path
    }

    def addPath(path: Path) {
        paths = paths ::: List(path)
    }

    /**
     *  Removes all stroke points from this stroke.
     */
    def clear() {
        paths = Nil
    }


    def updateSurface(surface: RenderSurface) = {

        paths foreach (_.render(surface))


    }


}