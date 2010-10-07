package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import org.flowpaint.pixelprocessor.PixelProcessor
import org.flowpaint.util.DataSample

/**
 * A 1/(x*c) curve, located so that f(0) = 0 and f(1) = 1.
 * By varying the c parameter, different curved slopes can be constructed, from linear, towards a sharp corner.
 * 
 * @author Hans Haggstrom
 */

class Curve extends PixelProcessor("","","""
    float value$id$ = $getScaleOffsetFloat value, 0f$;
    float curvature$id$ = $getScaleOffsetFloat curvature, 0f$;

    if ( curvature$id$ == 0.5f )
    {
        $setScaleOffsetFloat result$ value$id$;
    }
    else if ( curvature$id$ <= 0f )
    {
        $setScaleOffsetFloat result$ 1f;
    }
    else if ( curvature$id$ >= 1f )
    {
        $setScaleOffsetFloat result$ 0f;
    }
    else
    {
        // Clamp value
        if (value$id$ < 0f) value$id$ = 0f;
        if (value$id$ > 1f) value$id$ = 1f;

        // Scale curvature
        curvature$id$ = (curvature$id$ - 0.5f) * 10;
        curvature$id$ = curvature$id$ * curvature$id$ * curvature$id$ * curvature$id$ * curvature$id$;

        final float sign$id$ = (curvature$id$ < 0f) ? -1f : 1f;
        curvature$id$ = curvature$id$ * sign$id$;

        final float t$id$ = (float) ( curvature$id$ * (-0.5f) * (1f + sign$id$ * Math.sqrt( 1f + 4f / curvature$id$ ) ) );

        final float result$id$ = 1f - ( 1f / ( (value$id$ + (1/curvature$id$) * t$id$) * curvature$id$ ) - t$id$ / curvature$id$ );

        $setScaleOffsetFloat result$ result$id$;
    }

  """)


/*

        def curve(value : Float, curvature : Float) : Float = {
            val t = ( curvature * -0.5f * (1f + Math.sqrt( 1f + 4f / curvature ) ) ).toFloat

            1f - ( 1f / ( curvature * value + t ) + t / curvature )
        }
*/
