package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import pixelprocessor.PixelProcessor
import _root_.scala.collection.Map
import util.DataSample

/**
 *
 * 
 * @author Hans Haggstrom
 */
class Copy extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) = {

    val from = getStringProperty( "from", null )
    val to = getStringProperty( "to", null )

    if (from != null && to != null) {
      val value = getMappedVar( from, 0, variables, variableNameMappings )
      setMappedVar( to, value, variables, variableNameMappings )
    }
  }

}