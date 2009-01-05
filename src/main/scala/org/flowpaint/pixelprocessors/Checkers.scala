package org.flowpaint.pixelprocessors

import _root_.scala.collection.Map
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

class Checkers extends PixelProcessor {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String]) {

    val value1 = getMappedVar( "value1", 0.3333f, variables, variableNameMappings )
    val value2 = getMappedVar( "value2", 0.6666f, variables, variableNameMappings )
    val gridSize = getMappedVar( "gridSize", 16f, variables, variableNameMappings )
    val gridSizeX = getMappedVar( "gridSizeX", gridSize, variables, variableNameMappings )
    val gridSizeY = getMappedVar( "gridSizeY", gridSize, variables, variableNameMappings )

    val x = getMappedVar( "x", 0f, variables, variableNameMappings )
    val y = getMappedVar( "y", 0f, variables, variableNameMappings )

    def onBand(c: Float, size : Float): Boolean = if (size == 0) false else ((c / size).toInt) % 2 == 0
    val value = if (onBand(x, gridSizeX) != onBand(y, gridSizeY)) v1 else v2

    setMappedVar( "result", value, variables, variableNameMappings )
  }
}