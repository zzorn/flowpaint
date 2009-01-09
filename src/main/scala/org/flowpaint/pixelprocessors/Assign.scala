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
class Assign extends PixelProcessor("","") {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) = {

    // Set specified float values to specified variables
    setMappedVars( getSettings.getFloatProperties, variables, variableNameMappings )

    // Set variables with text value to the value of the referenced variable
    val outputs = getSettings.getPropertyNames map {name : String => {
      val value = getMappedVar( name, 0, variables, variableNameMappings )

      (name, value)
    }}

    outputs foreach { v:( String,Float ) => setMappedVarWithoutRedirect( v._1, v._2, variables, variableNameMappings )  }
  }

}