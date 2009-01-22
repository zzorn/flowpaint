package org.flowpaint.pixelprocessor
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

class SingleFunctionPixelProcessor( function : String ) extends PixelProcessor("","",
  """
    $setScaleOffsetFloat result$ ( (float) ( """ + function + """ ( $getScaleOffsetFloat value$ ) ) );
  """)



