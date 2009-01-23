package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.{DataSample, MathUtils}
import pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Interpolate extends PixelProcessor("","","""
    final float input$id$        = $getScaleOffsetFloat input, 0f$;
    final float inputStart$id$   = $getScaleOffsetFloat inputStart, 0f$;
    final float inputEnd$id$     = $getScaleOffsetFloat inputEnd, 1f$;
    final float outputStart$id$  = $getScaleOffsetFloat outputStart, 0f$;
    final float outputEnd$id$    = $getScaleOffsetFloat outputEnd, 1f$;

    float result$id$;

    // Check for special case where start and end positions are the same.  In this case return the average value.
    if ( inputStart$id$ == inputEnd$id$ )
    {
        result$id$ = 0.5f * ( outputStart$id$ + outputEnd$id$ );
    }
    else {
        final float relativePosition$id$ =  ( input$id$ - inputStart$id$ ) / ( inputEnd$id$ - inputStart$id$ );
        result$id$ = outputStart$id$ + relativePosition$id$ * ( outputEnd$id$ - outputStart$id$ );
    }
    
    $setScaleOffsetFloat result$ result$id$;
  """) 