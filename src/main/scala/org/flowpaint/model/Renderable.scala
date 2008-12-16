package org.flowpaint.model

import renderer.RenderSurface

/**
 * Something that can be rendered to a surface.
 * 
 * @author Hans Haggstrom
 */
trait Renderable {

  def render( surface : RenderSurface )

}