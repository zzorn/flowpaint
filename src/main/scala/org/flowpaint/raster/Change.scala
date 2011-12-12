package org.flowpaint.model2.raster

import org.flowpaint.model2.Picture

/**
 * Stores a change done to a picture, to allow undoing and redoing it.
 */
trait Change {

  def undo(picture: Picture)
  def redo(picture: Picture)

  
}