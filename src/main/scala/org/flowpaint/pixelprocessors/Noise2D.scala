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

class Noise2D extends PixelProcessor(
    "", "",

    """
     float noiseX$id$ = $getScaleOffsetFloat noiseX, 0f$;
     float noiseY$id$ = $getScaleOffsetFloat noiseY, 0f$;

     float result$id$ = NoiseUtils.noise2( noiseX$id$, noiseY$id$ ) * 0.5f + 0.5f;

     $setScaleOffsetFloat result$ result$id$;

    """)  