package org.flowpaint.pixelprocessors

import _root_.scala.collection.Map
import util.DataSample

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Constant extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String]) = {

    val sourceNames = getSettings.getFloatPropertyNames

    sourceNames foreach {name : String => {
      val value = getFloatProperty( name, 0f )

      setMappedVar( name, value, variables, variableNameMappings )
    }}
  }
}