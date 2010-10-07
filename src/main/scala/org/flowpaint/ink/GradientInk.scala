package org.flowpaint.ink

import _root_.org.flowpaint.property.Data
import org.flowpaint.gradient.{MultiGradient, Gradient}
import org.flowpaint.FlowPaint
import org.flowpaint.util.{MathUtils, PropertyRegister}

object GradientInk {
    val defaultGradient = new MultiGradient("defaultGradient")
}

/**
 *
 *
 * @author Hans Haggstrom
 */
class GradientInk() extends Ink {

    def processPixel(pixelData: Data) {

        val gradient: Gradient = pixelData.getReference[Gradient]("gradient", GradientInk.defaultGradient, FlowPaint.library)
        val alphaPressure = pixelData.getFloatProperty("alphaPressure", 1)

        val positionAlongStroke = pixelData.getFloatProperty(PropertyRegister.POSTION_ALONG_STROKE, 0)
        val positionAcrossStroke = pixelData.getFloatProperty(PropertyRegister.POSITION_ACROSS_STROKE, 0)

        val strokeAlpha = pixelData.getFloatProperty(PropertyRegister.ALPHA, 1f)

        val data: Data = gradient(0.5f + 0.5f * positionAcrossStroke)

        val gradientAlpha: Float = data.getFloatProperty(PropertyRegister.ALPHA, 1)
        val pressure = pixelData.getFloatProperty(PropertyRegister.PRESSURE, 0.5f)
        val alpha = strokeAlpha * MathUtils.interpolate(alphaPressure, gradientAlpha, gradientAlpha * pressure)

        pixelData.setValuesFrom(data)
        pixelData.setFloatProperty(PropertyRegister.ALPHA, alpha)
    }

}