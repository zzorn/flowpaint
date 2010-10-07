package org.flowpaint.model

import org.flowpaint.util.ListenableList
import org.flowpaint.renderer.RenderSurface

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