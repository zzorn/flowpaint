package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

class Checkers extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val valueA = getMappedVar( "valueA", 0.3333f, variables, variableNameMappings )
    val valueB = getMappedVar( "valueB", 0.6666f, variables, variableNameMappings )
    val gridSize = getMappedVar( "gridSize", 16f, variables, variableNameMappings )
    val gridSizeX = getMappedVar( "gridSizeX", gridSize, variables, variableNameMappings )
    val gridSizeY = getMappedVar( "gridSizeY", gridSize, variables, variableNameMappings )

    val x = getMappedVar( "canvasX", 0f, variables, variableNameMappings )
    val y = getMappedVar( "canvasY", 0f, variables, variableNameMappings )

    def onBand(c: Float, size : Float): Boolean = if (size == 0) false else ((c / size).toInt) % 2 == 0
    val value = if (onBand(x, gridSizeX) != onBand(y, gridSizeY)) valueA else valueB

    setMappedVar( "result", value, variables, variableNameMappings )
  }
}