package org.flowpaint.model


import org.flowpaint.renderer.{PictureProvider, RenderSurface}
import java.util.ArrayList
import scala.collection.JavaConversions._

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