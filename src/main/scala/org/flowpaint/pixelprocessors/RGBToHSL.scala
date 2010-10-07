package org.flowpaint.pixelprocessors

import org.flowpaint.property.Data
import scala.collection.Map
import org.flowpaint.util.DataSample
import org.flowpaint.util.MathUtils._
import org.flowpaint.pixelprocessor.PixelProcessor


/**
 * 
 * 
 * @author Hans Haggstrom
 */

class RGBToHSL extends PixelProcessor("","",
    """
     float r$id$ = $getScaleOffsetFloat red, 0f$;
     float g$id$ = $getScaleOffsetFloat green, 0f$;
     float b$id$ = $getScaleOffsetFloat blue, 0f$;

     // Clamp / wrap to range
     if (r$id$ < 0f) r$id$ = 0f;
     if (r$id$ > 1f) r$id$ = 1f;
     if (g$id$ < 0f) g$id$ = 0f;
     if (g$id$ > 1f) g$id$ = 1f;
     if (b$id$ < 0f) b$id$ = 0f;
     if (b$id$ > 1f) b$id$ = 1f;

    final float max$id$ = Math.max(Math.max(r$id$, g$id$), b$id$);
    final float min$id$ = Math.min(Math.min(r$id$, g$id$), b$id$);
    float l$id$ = (max$id$ + min$id$) * 0.5f;
    float h$id$ = 0f;
    float s$id$ = 0f;

    if (max$id$ == min$id$) {
      // Greyscale
      h$id$ = 0f;
      s$id$ = 0f;
    }
    else {
      float  d$id$ = max$id$ - min$id$;
      s$id$ = (l$id$ > 0.5f) ? d$id$ / (2f - max$id$ - min$id$) : d$id$ / (max$id$ + min$id$);

      if (max$id$ == r$id$) h$id$ = (g$id$ - b$id$) / d$id$ + ((g$id$ < b$id$) ? 6f : 0f);
      else if (max$id$ == g$id$) h$id$ =(b$id$ - r$id$) / d$id$ + 2f;
      else h$id$ =(r$id$ - g$id$) / d$id$ + 4f;

      h$id$ /= 6f;
    }

    $setScaleOffsetFloat hue$ h$id$;
    $setScaleOffsetFloat saturation$ s$id$;
    $setScaleOffsetFloat lightness$ l$id$;

    """) 