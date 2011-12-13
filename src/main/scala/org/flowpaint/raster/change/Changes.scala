package org.flowpaint.raster.change

import org.flowpaint.raster.picture.Picture

/**
 * Several changes
 */
case class Changes(changes: List[Change]) extends Change {
  def redo(picture: Picture) {
    changes foreach (_.redo(picture))
  }

  def undo(picture: Picture) {
    changes foreach (_.undo(picture))
  }
}
