package org.flowpaint.ink

import org.flowpaint.property.Data
import org.flowpaint.gradient.Gradient
import org.flowpaint.FlowPaint
import org.flowpaint.util.{PerlinNoise, PropertyRegister, DataSample}

/**
 *
 *
 * @author Hans Haggstrom
 */

class NoiseInk extends Ink{
    def processPixel(pixelData: Data) {

        val gradient: Gradient = pixelData.getReference[Gradient]("gradient", GradientInk.defaultGradient, FlowPaint.library)
        val noiseScaleAcross = pixelData.getFloatProperty("noiseScaleAcross", 1)
        val noiseScaleAlong = pixelData.getFloatProperty("noiseScaleAlong", 1)
        val alphaWithDistance = pixelData.getFloatProperty("alphaWithDistance", 1)
        val propertyName = pixelData.getStringProperty("propertyName", "time")
        val octaves = pixelData.getFloatProperty("octaves", 1).toInt



        val positionAlongStroke = pixelData.getFloatProperty(PropertyRegister.POSTION_ALONG_STROKE, 0)
        val positionAcrossStroke = pixelData.getFloatProperty(PropertyRegister.POSITION_ACROSS_STROKE, 0)

        var u = pixelData.getFloatProperty(propertyName, 1) * noiseScaleAlong
        var v = positionAcrossStroke * noiseScaleAcross
        var w = pixelData.getFloatProperty(PropertyRegister.RANDOM_SEED, 0.5f) * 1000

        // Construct the noise from multiple samples (perlin turbulence)
        var n = 0f
        var i = 0
        var scale = 1f
        var amplitude = 1f
        while (i < octaves) {
            n += PerlinNoise.noise3(u * scale, v * scale, w) * amplitude
            scale *= 2f
            amplitude *= 0.5f
            u += 213.1234f
            v += 98054.564f
            w += 345.98745f
            i += 1
        }

        val noise: Data = gradient(0.5f + 0.5f * n)

        val noiseAlpha: Float = noise.getFloatProperty(PropertyRegister.ALPHA, 1)
        val distanceFromEdge = 1f - Math.abs(positionAcrossStroke)
        val alphaMul = if (distanceFromEdge >= alphaWithDistance) 1f else distanceFromEdge / alphaWithDistance
        val alpha = noiseAlpha * alphaMul * pixelData.getFloatProperty(PropertyRegister.ALPHA, 1)

        pixelData.setValuesFrom(noise)
        pixelData.setFloatProperty(PropertyRegister.ALPHA, alpha)
    }
}
