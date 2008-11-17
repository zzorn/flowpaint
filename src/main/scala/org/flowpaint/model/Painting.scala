package org.flowpaint.model

import _root_.scala.collection.jcl.ArrayList
import renderer.{PictureProvider, RenderSurface}
import util.PerformanceTester.time

/**
 *  The central datamodel for pictures in FlowPaint.
 *
 * @author Hans Haggstrom
 */
class Painting extends PictureProvider {
  var layers: ArrayList[Layer] = new ArrayList()

  clear()

  def currentLayer: Layer = layers(0)

  def updateSurface(surface: RenderSurface) = {

    time("Redrawing painting")
              {
                surface.clear()
                layers.foreach(layer => layer.updateSurface(surface))
              }

  }

  def clear() {
    layers.clear
    layers.add( new Layer )
  }
}



