package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

class Checkers extends PixelProcessor("","","""
    final float value1$id$ = $getScaleOffsetFloat value1, 0.3333f$;
    final float value2$id$ = $getScaleOffsetFloat value2, 0.66666f$;
    final float gridSizeX$id$  = $getScaleOffsetFloat gridSizeX, 16$;
    final float gridSizeY$id$  = $getScaleOffsetFloat gridSizeY, 16$;
    final float x$id$  = $getScaleOffsetFloat canvasX, 0f$;
    final float y$id$  = $getScaleOffsetFloat canvasY, 0f$;

    final boolean onBandX$id$ = (gridSizeX$id$ == 0) ? false : ((int)(x$id$ / gridSizeX$id$)) % 2 == 0;
    final boolean onBandY$id$ = (gridSizeY$id$ == 0) ? false : ((int)(y$id$ / gridSizeY$id$)) % 2 == 0;
    final float value$id$ = (onBandX$id$ != onBandY$id$) ? value1$id$ : value2$id$;

    $setScaleOffsetFloat result$ value$id$;
  """) 