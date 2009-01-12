package org.flowpaint.model


import _root_.scala.collection.jcl.ArrayList
import renderer.{PictureProvider, RenderSurface}

/**
 * A layer of a painting.
 *
 * @author Hans Haggstrom
 */
class Layer extends PictureProvider {

  private val strokes = new ArrayList[Stroke]

  def addStroke( stroke:Stroke ) {
    strokes.add( stroke )
  }

    def removeStroke( stroke:Stroke ) {
      strokes.remove( stroke )
    }

  def updateSurface(surface: RenderSurface) = {

    strokes.foreach( stroke => stroke.updateSurface(surface) )

  }
}