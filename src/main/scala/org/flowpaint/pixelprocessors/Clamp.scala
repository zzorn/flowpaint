package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import org.flowpaint.pixelprocessor.PixelProcessor
import org.flowpaint.util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Clamp extends PixelProcessor("","","""
    final float value$id$ = $getScaleOffsetFloat value, 0f$;
    final float min$id$ = $getScaleOffsetFloat min, 0f$;
    final float max$id$  = $getScaleOffsetFloat max, 1f$;

    final float result$id$ = value$id$ > max$id$ ? max$id$ : ( value$id$ < min$id$ ? min$id$ : value$id$);

    $setScaleOffsetFloat result$ result$id$;
  """)