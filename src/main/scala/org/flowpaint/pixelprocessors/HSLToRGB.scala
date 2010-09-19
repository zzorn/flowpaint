package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import org.flowpaint.util.{DataSample, MathUtils}
import org.flowpaint.util.MathUtils._
import org.flowpaint.pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class HSLToRGB  extends PixelProcessor("","",
  """
     float h$id$ = $getScaleOffsetFloat hue, 0f$;
     float s$id$ = $getScaleOffsetFloat saturation, 0f$;
     float l$id$ = $getScaleOffsetFloat lightness, 0f$;

     // Clamp / wrap to range
     h$id$ = h$id$ % 1f;
     if (h$id$ < 0) h$id$ += 1f;
     if (s$id$ < 0f) s$id$ = 0f;
     if (s$id$ > 1f) s$id$ = 1f;
     if (l$id$ < 0f) l$id$ = 0f;
     if (l$id$ > 1f) l$id$ = 1f;

    float r$id$ = 0f;
    float g$id$ = 0f;
    float b$id$ = 0f;

    if (l$id$ == 0f) {
      // Black
    }
    else if (l$id$ == 1f) {
      // White
      r$id$ = 1f;
      g$id$ = 1f;
      b$id$ = 1f;
    }
    else if (s$id$ == 0f) {
      // Greyscale
      r$id$ = l$id$;
      g$id$ = l$id$;
      b$id$ = l$id$;
    }
    else {
      // Arbitrary color

      float q$id$ =  (l$id$ < 0.5f) ? (l$id$ * (1f + s$id$)) : (l$id$ + s$id$ - l$id$ * s$id$);
      float p$id$ = 2f * l$id$ - q$id$;

      float th$id$;

      // Red
      th$id$ = h$id$ + 1f / 3f;
      if (th$id$ < 0f) th$id$ += 1f;
      if (th$id$ > 1f) th$id$ -= 1f;
      if (th$id$ < 1f / 6f) r$id$ = p$id$ + (q$id$ - p$id$) * 6f * th$id$;
      else if (th$id$ < 1f / 2f) r$id$ = q$id$;
      else if (th$id$ < 2f / 3f) r$id$ = p$id$ + (q$id$ - p$id$) * (2f / 3f - th$id$) * 6f;
      else r$id$ = p$id$;

      // Green
      th$id$ = h$id$;
      if (th$id$ < 0f) th$id$ += 1;
      if (th$id$ > 1f) th$id$ -= 1;
      if (th$id$ < 1f / 6f) g$id$ = p$id$ + (q$id$ - p$id$) * 6f * th$id$;
      else if (th$id$ < 1f / 2f) g$id$ = q$id$;
      else if (th$id$ < 2f / 3f) g$id$ = p$id$ + (q$id$ - p$id$) * (2f / 3f - th$id$) * 6f;
      else g$id$ = p$id$;


      // Blue
      th$id$ = h$id$ - 1f / 3f;
      if (th$id$ < 0f) th$id$ += 1;
      if (th$id$ > 1f) th$id$ -= 1;
      if (th$id$ < 1f / 6f) b$id$ = p$id$ + (q$id$ - p$id$) * 6f * th$id$;
      else if (th$id$ < 1f / 2f) b$id$ = q$id$;
      else if (th$id$ < 2f / 3f) b$id$ = p$id$ + (q$id$ - p$id$) * (2f / 3f - th$id$) * 6f;
      else b$id$ = p$id$;

    }

    $setScaleOffsetFloat red$ r$id$;
    $setScaleOffsetFloat green$ g$id$;
    $setScaleOffsetFloat blue$ b$id$;
  """)