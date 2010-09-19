package org.flowpaint.ink

import org.flowpaint.property.Data
import org.flowpaint.util.DataSample

/**
 *  Used for debugging stroke rendering.
 *
 * @author Hans Haggstrom
 */
class DebugInk() extends Ink {
    def processPixel(pixelData: Data) = {

        val alongFactor = getFloatProperty("alongFactor", 1)
        val acrossFactor = getFloatProperty("acrossFactor", 1)

        val positionAlongStroke = pixelData.getFloatProperty("positionAlongStroke", 0)
        val positionAcrossStroke = pixelData.getFloatProperty("positionAcrossStroke", 0)

        pixelData.setFloatProperty("red", positionAlongStroke * alongFactor)
        pixelData.setFloatProperty("green", 0f)
        pixelData.setFloatProperty("blue", (0.5f + 0.5f * positionAcrossStroke) * acrossFactor)
        pixelData.setFloatProperty("alpha", 1)


    }
}