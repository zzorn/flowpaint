package org.flowpaint.pixelprocessors
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import org.flowpaint.pixelprocessor.PixelProcessor
import org.flowpaint.util.{DataSample, MathUtils}
/**
 * 
 *
 * @author Hans Haggstrom
 */

class InterpolateSmoothly extends PixelProcessor("","",
  """
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
    else if ( input$id$ >= inputEnd$id$ )
    {
        result$id$ = outputEnd$id$;
    }
    else if ( input$id$ <= inputStart$id$ )
    {
        result$id$ = outputStart$id$;
    }
    else {
        final float relativePosition$id$ =  ( input$id$ - inputStart$id$ ) / ( inputEnd$id$ - inputStart$id$ );
        final float t$id$ = ((float)(1f - Math.cos(relativePosition$id$ * Math.PI))) * 0.5f;
        result$id$ = outputStart$id$ + t$id$ * ( outputEnd$id$ - outputStart$id$ );
    }

    $setScaleOffsetFloat result$ result$id$;
  """ ) 
