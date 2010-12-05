package org.flowpaint.util

import org.flowpaint.model2.raster.Change
import org.flowpaint.model2.{Operation, Picture}

/**
 * A command to run a rendering operation.
 */
class OperationCommand(operation: Operation) extends Command[Picture](
  operation.description,
  // Action
  (picture: Picture) => {
    val tiles = operation.getAffectedTiles(picture)
    operation.renderToTiles(picture, tiles)
    picture.takeUndoSnapshot()
  },
  // Undo action
  (picture: Picture, undoData: Object) => {
    val change = undoData.asInstanceOf[Change]
    change.undo(picture)
    change
  },
  // Redo action
  (picture: Picture, redoData: Object) => {
    val change = redoData.asInstanceOf[Change]
    change.redo(picture)
    change
  },
  // Can undo
  (picture: Picture) => {true}
  ) {

}