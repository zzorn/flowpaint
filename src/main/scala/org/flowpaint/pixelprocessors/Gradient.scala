package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.{Data, DataImpl}
import _root_.scala.collection.Map
import util.DataSample
import pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Gradient extends PixelProcessor {

  private val temp = new DataImpl()

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val gradientName = generalSettings.getStringProperty( "gradient", getStringProperty( "gradient", null ) )

    val gradient : org.flowpaint.gradient.Gradient = if (gradientName == null) null else FlowPaint.library.getTome[org.flowpaint.gradient.Gradient]( gradientName, null )

    if (gradient != null) {

      val gradientPosition = getMappedVar( "gradientPosition", 0.5f, variables, variableNameMappings )

      temp.clear
      gradient.getValue( gradientPosition, temp )

      setMappedVars( temp.getFloatProperties, variables, variableNameMappings )
    }
  }
}