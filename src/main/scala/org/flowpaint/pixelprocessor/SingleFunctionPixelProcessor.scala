package org.flowpaint.pixelprocessor
import org.flowpaint.property.Data
import scala.collection.Map
import org.flowpaint.util.DataSample

/**
 * 
 *
 * @author Hans Haggstrom
 */

class SingleFunctionPixelProcessor( function : String ) extends PixelProcessor("","",
  """
    $setScaleOffsetFloat result$ ( (float) ( """ + function + """ ( $getScaleOffsetFloat value$ ) ) );
  """)



