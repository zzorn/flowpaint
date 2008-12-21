package org.flowpaint.ink

import _root_.org.flowpaint.property.Data
import util.DataSample

/**
 *  Used for debugging stroke rendering.
 *
 * @author Hans Haggstrom
 */
class DebugInk() extends Ink {
    def processPixel(pixelData: Data) = {

        val alongFactor = settings.getFloatProperty("alongFactor", 1)
        val acrossFactor = settings.getFloatProperty("acrossFactor", 1)

        val positionAlongStroke = pixelData.getFloatProperty("positionAlongStroke", 0)
        val positionAcrossStroke = pixelData.getFloatProperty("positionAcrossStroke", 0)

        pixelData.setFloatProperty("red", positionAlongStroke * alongFactor)
        pixelData.setFloatProperty("green", 0f)
        pixelData.setFloatProperty("blue", (0.5f + 0.5f * positionAcrossStroke) * acrossFactor)
        pixelData.setFloatProperty("alpha", 1)


    }
}