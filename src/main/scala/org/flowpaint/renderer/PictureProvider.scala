package org.flowpaint.renderer


/**
 * Provides picture pixel data on request.
 *
 * @author Hans Haggstrom
 */
trait PictureProvider {

  def updateSurface( surface: RenderSurface  )


}