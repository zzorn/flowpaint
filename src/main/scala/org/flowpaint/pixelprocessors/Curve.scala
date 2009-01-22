package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 * A 1/(x*c) curve, located so that f(0) = 0 and f(1) = 1.
 * By varying the c parameter, different curved slopes can be constructed, from linear, towards a sharp corner.
 * 
 * @author Hans Haggstrom
 */

class Curve extends PixelProcessor("","","""
    float value$id$ = $getScaleOffsetFloat value, 0f$;
    float curvature$id$ = $getScaleOffsetFloat curvature, 0f$;

    final float sign$id$ = (curvature$id$ < 0f) ? -1f : 1f;
    if ( curvature$id$ < 0f) curvature$id$ = -curvature$id$;

    if ( curvature$id$ == 0f )
    {
        $setScaleOffsetFloat result$ value$id$;
    }
    else
    {
        // Scale input a bit
/*
        value$id$ = value$id$ - 0.5f;
*/
        value$id$ = value$id$ * 10f;
        value$id$ = value$id$ * value$id$ * value$id$;

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
