package org.flowpaint.raster.tile


/**
 * 
 */
object TileService {

  // Tilesize of 1 << 6 == 64
  val tileWidthShift = 6
  val tileHeightShift = 6

  val tileWidth = 1 << tileWidthShift
  val tileHeight = 1 << tileHeightShift

  val tilePixels: Int = tileWidth * tileHeight

  def tileIdForLocation(canvasX: Int, canvasY: Int): TileId = TileId.forLocation(canvasX, canvasY)

  // TODO: Implement tile pooling
  def allocateDataTile(): DataTile = new DataTile()
  def freeDataTile(tile: DataTile) = {}

  def allocateDataTile(sourceTile: Tile): DataTile = {
    val newTile = allocateDataTile()

    sourceTile match {
      case dataTile: DataTile =>
        newTile.copyDataFrom(dataTile)
      case valueTile: SingleValueTile =>
        val v = valueTile.value
        var i = newTile.data.length - 1
        while (i >= 0) {
          newTile.data(i) = v
          i -= 1
        }
    }

    newTile
  }
}