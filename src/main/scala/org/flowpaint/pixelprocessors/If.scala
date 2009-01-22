package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 * Provides an if clause with up to 5 thresholds.
 * The value is tested to see if it is smaller than each threshold, if so, the value for that threshold is returned.
 * 
 * @author Hans Haggstrom
 */

class If extends PixelProcessor("","","""

    final float value$id$ = $getScaleOffsetFloat value, 0f$;
    final float upperThreshold1$id$ = $getScaleOffsetFloat threshold1, Float.NaN$;
    final float thresholdValue1$id$ = $getScaleOffsetFloat value1, 0f$;
    final float upperThreshold2$id$ = $getScaleOffsetFloat threshold2, Float.NaN$;
    final float thresholdValue2$id$ = $getScaleOffsetFloat value2, 0f$;
    final float upperThreshold3$id$ = $getScaleOffsetFloat threshold3, Float.NaN$;
    final float thresholdValue3$id$ = $getScaleOffsetFloat value3, 0f$;
    final float upperThreshold4$id$ = $getScaleOffsetFloat threshold4, Float.NaN$;
    final float thresholdValue4$id$ = $getScaleOffsetFloat value4, 0f$;
    final float upperThreshold5$id$ = $getScaleOffsetFloat threshold5, Float.NaN$;
    final float thresholdValue5$id$ = $getScaleOffsetFloat value5, 0f$;
    final float elseValue$id$ = $getScaleOffsetFloat elseValue, 0f$;

    float result$id$ = elseValue$id$;
    if ( value$id$ < upperThreshold1$id$ ) result$id$ = thresholdValue1$id$;
    else if ( value$id$ < upperThreshold2$id$ ) result$id$ = thresholdValue2$id$;
    else if ( value$id$ < upperThreshold3$id$ ) result$id$ = thresholdValue3$id$;
    else if ( value$id$ < upperThreshold4$id$ ) result$id$ = thresholdValue4$id$;
    else if ( value$id$ < upperThreshold5$id$ ) result$id$ = thresholdValue5$id$;

    $setScaleOffsetFloat result$ result$id$;
  """ ) 