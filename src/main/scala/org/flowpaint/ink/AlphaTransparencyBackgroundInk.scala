package org.flowpaint.ink
import util.DataSample

/**
 * A checker-pattern ink, using the absolue screen coordinates (=used for alpha transparency background).
 * 
 * @author Hans Haggstrom
 */
class AlphaTransparencyBackgroundInk extends Ink {

  private val v1 = 0.3333f
  private val v2 = 0.6666f
  private val gridSize = 16

  def processPixel(pixelData: DataSample) {

    def onBand( c : Float ) : Boolean = ((c.toInt / gridSize) % 2 == 0 )

    // Determine grey level
    val x = pixelData.getProperty("screenX", 0)
    val y = pixelData.getProperty("screenY", 0)
    val v = if ( onBand(x) != onBand(y) ) v1 else v2

    // Mix the original color with the checker background
    val alpha = pixelData.getProperty("alpha", 0)
    val grey = (1f - alpha) * v
    val r = alpha * pixelData.getProperty("red", 0) + grey
    val g = alpha * pixelData.getProperty("green", 0) + grey
    val b = alpha * pixelData.getProperty("blue", 0) + grey
    val a = 1f

    pixelData.setProperty( "red", r )
    pixelData.setProperty( "green", g )
    pixelData.setProperty( "blue", b )
    pixelData.setProperty( "alpha", a )
  }
}