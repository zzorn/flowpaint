package org.flowpaint.ink
import util.DataSample
import util.PropertyRegister

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
    val x = pixelData.getProperty(PropertyRegister.SCREEN_X, 0)
    val y = pixelData.getProperty(PropertyRegister.SCREEN_Y, 0)
    val v = if ( onBand(x) != onBand(y) ) v1 else v2

    // Mix the original color with the checker background
    val alpha = pixelData.getProperty(PropertyRegister.ALPHA, 0)
    val grey = (1f - alpha) * v
    val r = alpha * pixelData.getProperty(PropertyRegister.RED, 0) + grey
    val g = alpha * pixelData.getProperty(PropertyRegister.GREEN, 0) + grey
    val b = alpha * pixelData.getProperty(PropertyRegister.BLUE, 0) + grey
    val a = 1f

    pixelData.setProperty( PropertyRegister.RED, r )
    pixelData.setProperty( PropertyRegister.GREEN, g )
    pixelData.setProperty( PropertyRegister.BLUE, b )
    pixelData.setProperty( PropertyRegister.ALPHA, a )
  }
}