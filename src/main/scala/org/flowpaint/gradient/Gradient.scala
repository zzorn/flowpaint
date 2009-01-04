package org.flowpaint.gradient

import _root_.scala.xml.Elem
import property.{DataImpl, Data}
import util.{DataSample, AbstractTome, Tome}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

trait Gradient extends Tome {

  /**
   * Map a floating point value between 0 and 1 to a DataSample, representing a color or other channel values for a picture
   * If the input value is less than zero or greater than one it is clamped to that range.  If it is NaN it is treated as zero.
   */
  final def apply( t : Float ) : Data = {

    // TODO, better NaN check?
    val zeroToOne  = if (t < 0f || t != t ) 0f
    else if (t > 1f) 1f
    else t

    val data = new DataImpl()
    getValue( zeroToOne, data  )

    data
  }

  /**
   * Map a floating point value between 0 and 1 to a DataSample, representing a color or other channel values for a picture
   */
  def getValue( zeroToOne : Float, outputData : Data )

  
}