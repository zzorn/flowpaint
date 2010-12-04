package org.flowpaint.model2.raster

import org.flowpaint.model2.Picture

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
