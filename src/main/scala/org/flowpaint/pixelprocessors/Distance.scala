package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import org.flowpaint.pixelprocessor.PixelProcessor
import org.flowpaint.util.{DataSample, MathUtils}
/**
 * Calculates pythagoras distance formula.  Useful for circles and radial effects.
 * 
 * @author Hans Haggstrom
 */

class Distance extends PixelProcessor("","","""
    final float x1$id$ = $getScaleOffsetFloat x1, 0f$;
    final float y1$id$ = $getScaleOffsetFloat y1, 0f$;
    final float x2$id$ = $getScaleOffsetFloat x2, 0f$;
    final float y2$id$ = $getScaleOffsetFloat y2, 0f$;

    final float xDiff$id$ = x2$id$ - x1$id$;
    final float yDiff$id$ = y2$id$ - y1$id$;
    final float result$id$ = (float) Math.sqrt( xDiff$id$ * xDiff$id$ + yDiff$id$ * yDiff$id$ );

    $setScaleOffsetFloat result$ result$id$;
  """) 