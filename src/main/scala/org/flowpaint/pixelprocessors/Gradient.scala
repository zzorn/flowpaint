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

  // TODO: Add scaling and offset (and wrap?) properties for input
  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val gradientProperty = getStringProperty( "gradientProperty", "gradient")
    val defaultGradient = getStringProperty( "gradient", null)

    val gradientIdentifier = generalSettings.getStringProperty( gradientProperty, defaultGradient )

    val gradient : org.flowpaint.gradient.Gradient = if (gradientIdentifier == null) null else FlowPaint.library.getTome[org.flowpaint.gradient.Gradient]( gradientIdentifier, null )

    if (gradient != null) {

      val gradientPosition = getScaleOffsetVar( "gradientPosition", 0.5f, variables, variableNameMappings )

      temp.clear
      gradient.getValue( gradientPosition, temp )

      setMappedVars( temp.getFloatProperties, variables, variableNameMappings )
    }
  }
}