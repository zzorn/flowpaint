package org.flowpaint.raster.change

import org.flowpaint.raster.tile.{TileId, Tile, DataTile}
import org.flowpaint.raster.picture.Picture
import org.flowpaint.raster.channel.Channel


/**
 * Some change to a tile channel
 */
case class TileChange(layer: Symbol, channel: Symbol,
                      oldTiles: Map[TileId, DataTile],
                      newTiles: Map[TileId, DataTile],
                      oldDefaultTile: Tile,
                      newDefaultTile: Tile) extends Change {

  def defaultTileChanged: Boolean = oldDefaultTile != null

  def undo(picture: Picture) {
    getChannel(picture).undoChange(this)
    picture.onPictureChanged()
  }

  def redo(picture: Picture) {
    getChannel(picture).redoChange(this)
    picture.onPictureChanged()
  }

  private def getChannel(picture: Picture): Channel = {
    picture.layer(layer).get.channel(channel).get
  }

}