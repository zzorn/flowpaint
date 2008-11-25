package org.flowpaint.ui

import _root_.org.flowpaint.brush.Brush
import _root_.org.flowpaint.util.DataSample
import java.awt.Graphics
import renderer.SingleRenderSurface

/**
 * 
 *
 * @author Hans Haggstrom
 */

class SliderUi( editedData : DataSample,
                editedParameter : String,
                startValue : Float,
                endValue : Float,
                previewBrush : => Brush ) extends ParameterUi(editedData) {

  // TODO: Slider implementation, with a brush used to render the preview

  // TODO: Reuse BrushPreview

  val surface = new SingleRenderSurface()
  val paintPanel = new PaintPanel( surface, false )

//  previewBrush().

}