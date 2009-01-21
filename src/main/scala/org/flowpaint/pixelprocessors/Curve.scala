package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 * A 1/(x*c) curve, located so that f(0) = 0 and f(1) = 1.
 * By varying the c parameter, different curved slopes can be constructed, from linear, towards a sharp corner.
 * 
 * @author Hans Haggstrom
 */

class Curve extends PixelProcessor("","","""
    final float value$id$ = $getScaleOffsetFloat value, 0f$;
    final float curvature$id$ = $getScaleOffsetFloat value, 0f$;

    if ( curvature$id$ == 0f )
    {
        $setScaleOffsetFloat result$ value$id$;
    }
    else
    {
        final float t$id$ = (float) ( curvature$id$ * -0.5f * (1f + Math.sqrt( 1f + 4f / curvature$id$ ) ) );

        final float result$id$ = 1f - ( 1f / ( curvature$id$ * value$id$ + t$id$ ) + t$id$ / curvature$id$ );

        $setScaleOffsetFloat result$ result$id$;
    }

  """) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {}

}