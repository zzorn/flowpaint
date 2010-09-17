package org.flowpaint.filters

import org.flowpaint.property.Data
import org.flowpaint.util.{MathUtils, PropertyRegister, DataSample}

/**
 *
 *
 * @author Hans Haggstrom
 */

class RadiusFromPressureFilter() extends PathProcessor {
    protected def processPathPoint(pointData: Data) : List[Data] = {
      
      val pressureEffect = pointData.getFloatProperty("pressureEffectOnSize", 1)

        // NOTE: Workaround for the bug of not registering tablet pressure for first point
        val defaultPressure = if (firstPoint) 0f else 0.5f

        val pressure = pointData.getFloatProperty(PropertyRegister.PRESSURE, defaultPressure)
        val maxRadius = getFloatProperty(PropertyRegister.MAX_RADIUS, pointData, 10)

        val radius = MathUtils.interpolate(pressureEffect, maxRadius, pressure * maxRadius)

        pointData.setFloatProperty(PropertyRegister.RADIUS, radius)

        List(pointData)
    }

}