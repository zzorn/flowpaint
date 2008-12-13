package org.flowpaint.model


import brush.Brush
import filters.StrokeListener
import renderer.{PictureProvider, RenderSurface}
import renderer.StrokeRenderer
import scala.collection.jcl.ArrayList
import util.{DataSample, RectangleInt}

/**
 *     A brush stroke on some layer.
 *
 * @author Hans Haggstrom
 */
case class Stroke(brush: Brush) extends PictureProvider {

  private var paths : List[Path] = Nil


/*
  private val points: ArrayList[DataSample] = new ArrayList[DataSample]()


  */
/**
   *   Adds a stroke point.  Doesn't update the picture.
   */
/*
  def addPoint(data: DataSample) {
    points.add(data)
  }
*/

  def addPath( path : Path  ) {
    paths = paths ::: path
  }

  /**
   * Removes all stroke points from this stroke.
   */
  def clear() {
    paths = Nil
  }
/*

  */
/**
   *   Adds a stroke point and updates the render surface with the latest stroke segment
   */
/*
  def addPoint(data: DataSample, surface: RenderSurface) {

    addPoint(data)

    renderStroke(surface, false)

  }
*/


  def updateSurface(surface: RenderSurface) = {

    paths foreach ( _.renderPath(surface) )


  }


}