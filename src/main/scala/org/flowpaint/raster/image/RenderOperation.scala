package org.flowpaint.raster.image

import org.flowpaint.model2.RasterRenderer
import org.flowpaint.raster.channel.Channel
import org.flowpaint.raster.channel.Raster
import org.flowpaint.util.Rectangle
import org.flowpaint.raster.channel.{Channel, Raster}
import org.flowpaint.util.Rectangle
import org.flowpaint.raster.tasks.Operation
import org.flowpaint.raster.tile.{TileService, TileId}

/**
 * 
 */
case class RenderOperation(tiles: Set[TileId],
                           source: Raster,
                           destination: FastImage,
                           renderAlphaAsCheckers: Boolean = true) extends Operation {


  var alphaGridSize: Int = 16
  var alphaLuminance1 : Float = 0.5f
  var alphaLuminance2: Float = 0.7f

  var redChannel   = 'red
  var greenChannel = 'green
  var blueChannel  = 'blue
  var alphaChannel = 'alpha

  def description = "Rendering picture"

  def affectedTiles = tiles

  def doOperation(tileId: TileId) {
    // This algorithm isn't really a very optimal way to do this,
    // but its a bit hard to optimize due to the the block based design (which is needed for memory optimization reasons)

    val red: Channel = source.channels(redChannel)
    val green: Channel = source.channels(greenChannel)
    val blue: Channel = source.channels(blueChannel)
    val alpha: Channel = source.channels(alphaChannel)

    val rTile = red.getTile(tileId)
    val bTile = green.getTile(tileId)
    val gTile = blue.getTile(tileId)
    val aTile = alpha.getTile(tileId)

    val target = destination.buffer

    var x = 0
    var y = 0
    var i = 0
    var di = 0
    while (y < TileService.tileHeight) {
      x = 0
      di = tileId.y1 * destination.width

      if (renderAlphaAsCheckers)
        // Render alpha value as a solid checkerboard pattern behind visible content
        while (x < TileService.tileWidth) {

          val a = aTile(i)
          if (a >= 1f) {
            // Solid
            target(di) = 0xFF000000 |
                         (rTile.getByte(i) << 16) |
                         (gTile.getByte(i) << 8) |
                          bTile.getByte(i)
          }
          else {
            // Calculate checkerboard alpha pattern if we have some transparency
            val alphaLuminance = if ((x % alphaGridSize * 2 < alphaGridSize) ==
                                     (y % alphaGridSize * 2 < alphaGridSize))
                                   alphaLuminance2
                                 else
                                   alphaLuminance1
            val preMultipliedAlphaLuminance = (1f - a) * alphaLuminance

            val r = (255 * (preMultipliedAlphaLuminance + rTile(i) * a)).toInt
            val g = (255 * (preMultipliedAlphaLuminance + gTile(i) * a)).toInt
            val b = (255 * (preMultipliedAlphaLuminance + bTile(i) * a)).toInt

            target(di) = 0xFF000000 | (r << 16) | (g << 8) | b
          }

          i += 1
          x += 1
          di += 1
        }
      else
        // Render alpha value into highest byte
        while (x < TileService.tileWidth) {
          target(di) = (aTile.getByte(i) << 24) |
                       (rTile.getByte(i) << 16) |
                       (gTile.getByte(i) <<  8) |
                        bTile.getByte(i)

          i += 1
          x += 1
          di += 1
        }

      y += 1
    }

    // TODO: Notify to allow image to repaint?
  }

}


