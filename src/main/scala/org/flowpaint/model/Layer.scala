package org.flowpaint.model


import renderer.{PictureProvider, RenderSurface}

/**
 * A layer of a painting.
 *
 * @author Hans Haggstrom
 */
class Layer extends PictureProvider {

  private var strokes : List[Stroke] = Nil

  def addStroke( stroke:Stroke ) {
    strokes = stroke :: strokes
  }

  def updateSurface(surface: RenderSurface) = {

    strokes.foreach( stroke => stroke.updateSurface(surface) )

  }
}