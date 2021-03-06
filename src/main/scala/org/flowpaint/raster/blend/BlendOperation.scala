package org.flowpaint.raster.blend

import org.flowpaint.raster.tasks.Operation
import org.flowpaint.raster.tile.TileId
import org.flowpaint.raster.layer.Layer
import org.flowpaint.raster.channel.Channel

/**
 * Wraps a blend along with the target of it in an operation, allowing it to be executed in multi core fashion.
 */
case class BlendOperation(blender: Blender,
                          tiles: Set[TileId],
                          targetChannel: Channel,
                          topChannel: Channel,
                          alphaChannel: Channel) extends Operation {

  def description = blender.name

  def affectedTiles = tiles

  def doOperation(tileId: TileId) {
    blender.blendData(targetChannel.getTileForModification(tileId),
                      topChannel.getTile(tileId),
                      alphaChannel.getTile(tileId))
  }
}