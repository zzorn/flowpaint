package org.flowpaint.model


import brush.Brush
import filters.StrokeListener
import renderer.{PictureProvider, RenderSurface}
import renderer.StrokeRenderer
import util.{DataSample, RectangleInt}

/**
 *      A brush stroke on some layer.
 *
 * @author Hans Haggstrom
 */
case class Stroke(brush: Brush) extends PictureProvider {

    private var paths: List[Path] = Nil

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

        paths foreach (_.renderPath(surface))


    }


}