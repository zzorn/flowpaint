package org.flowpaint.model

import renderer.{PictureProvider, RenderSurface}

/**
 * The central datamodel for pictures in FlowPaint.
 *
 * @author Hans Haggstrom
 */
class Painting extends PictureProvider {

  var layers : List[Layer] = List( new Layer )

  def currentLayer : Layer = layers(0)

  def updateSurface(surface: RenderSurface) = {

    println ("Painting update "+surface)

    surface.clear()

    layers.foreach( layer => layer.updateSurface(surface) )

  }
}



