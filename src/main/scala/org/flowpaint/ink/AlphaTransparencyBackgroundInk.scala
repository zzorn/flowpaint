package org.flowpaint.ink

import org.flowpaint.property.Data
import org.flowpaint.util.DataSample
import org.flowpaint.util.PropertyRegister

/**
 *  A checker-pattern ink, using the absolue screen coordinates (=used for alpha transparency background).
 *
 * @author Hans Haggstrom
 */
class AlphaTransparencyBackgroundInk extends Ink {

    def processPixel(pixelData: Data) {

        val v1 = getFloatProperty("lightGridLuminance", 0.3333f)
        val v2 = getFloatProperty("darkGridLuminance", 0.6666f)
        val gridSize = getFloatProperty("gridSize", 16)

        def onBand(c: Float): Boolean = ((c.toInt / gridSize) % 2 == 0)

        // Determine grey level
        val x = pixelData.getFloatProperty(PropertyRegister.SCREEN_X, 0)
        val y = pixelData.getFloatProperty(PropertyRegister.SCREEN_Y, 0)
        val v = if (onBand(x) != onBand(y)) v1 else v2

        // Mix the original color with the checker background
        val alpha = pixelData.getFloatProperty(PropertyRegister.ALPHA, 0)
        val grey = (1f - alpha) * v
        val r = alpha * pixelData.getFloatProperty(PropertyRegister.RED, 0) + grey
        val g = alpha * pixelData.getFloatProperty(PropertyRegister.GREEN, 0) + grey
        val b = alpha * pixelData.getFloatProperty(PropertyRegister.BLUE, 0) + grey
        val a = 1f

        pixelData.setFloatProperty(PropertyRegister.RED, r)
        pixelData.setFloatProperty(PropertyRegister.GREEN, g)
        pixelData.setFloatProperty(PropertyRegister.BLUE, b)
        pixelData.setFloatProperty(PropertyRegister.ALPHA, a)
    }
}