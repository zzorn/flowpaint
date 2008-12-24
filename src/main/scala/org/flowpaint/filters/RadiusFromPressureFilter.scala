package org.flowpaint.filters

import property.Data
import util.DataSample


import util.PropertyRegister

/**
 *
 *
 * @author Hans Haggstrom
 */

class RadiusFromPressureFilter() extends PathProcessor {
    protected def processPathPoint(pointData: Data) : List[Data] = {
      
        val maxRadius = pointData.getFloatProperty("maxRadius", 30)
        val pressureEffect = settings.getFloatProperty("pressureEffect", 1)

        // NOTE: Workaround for the bug of not registering tablet pressure for first point
        val defaultPressure = if (firstPoint) 0f else 0.5f

        val pressure = pointData.getFloatProperty(PropertyRegister.PRESSURE, defaultPressure)
        val maxRadius2 = pointData.getFloatProperty(PropertyRegister.MAX_RADIUS, 10)

        val radius = util.MathUtils.interpolate(pressureEffect, maxRadius2, pressure * maxRadius2)

        pointData.setFloatProperty(PropertyRegister.RADIUS, radius)

        List(pointData)
    }

}