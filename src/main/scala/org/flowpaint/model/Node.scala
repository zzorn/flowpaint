package org.flowpaint.model

import renderer.RenderSurface
import util.ListenableList

/**
 * A node can contain various renderable objects.
 * 
 * @author Hans Haggstrom
 */
// TODO: Add transformation that can be applied, and/or properties that can be set / modified for contained objects?
class Node extends Renderable {

    val children = new ListenableList[ Renderable ]


    def render(surface: RenderSurface)  {

      children.elements.foreach( _.render(surface) )

    }
}