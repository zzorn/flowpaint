package org.flowpaint.raster.change

import org.flowpaint.raster.picture.Picture


/**
 * Stores a change done to a picture, to allow undoing and redoing it.
 */
trait Change {

  def undo(picture: Picture)
  def redo(picture: Picture)

  
}