package org.flowpaint.model

import org.flowpaint.renderer.{RenderSurface, PictureProvider}

/**
 *   The central datamodel for pictures in FlowPaint.
 *
 * @author Hans Haggstrom
 */
class Painting extends PictureProvider {

  private var layers: List[Layer] = List( new Layer )

  var backgroundColor = java.awt.Color.WHITE

  def getLayers() : List[Layer] = layers

  def setLayers( newLayers : List[Layer] ) : Unit =  { layers = newLayers }

  def currentLayer: Layer = layers.head

  def updateSurface(surface: RenderSurface) = {


/* Just clearing to transparent.
    val color = if (backgroundColor == null) java.awt.Color.WHITE else backgroundColor
    surface.clearToColor( color )
*/

    surface.clear
    layers.foreach(layer => layer.updateSurface(surface))

  }

  def clear() {
      layers = List( new Layer )
  }
}



