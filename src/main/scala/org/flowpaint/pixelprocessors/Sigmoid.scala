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

class Sigmoid extends PixelProcessor("","",  """
    final float value$id$ = $getScaleOffsetFloat value1, 0f$;
    final float sharpness$id$ = $getScaleOffsetFloat value2, 0f$;

    final float result$id$ = 1.0f / (1.0f + (float)Math.exp( -6f * value$id$ * sharpness$id$ ));

    $setScaleOffsetFloat result$ result$id$;
  """) 


