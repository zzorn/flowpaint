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

class Checkers extends PixelProcessor("","","""
    final float value1$id$ = $getScaleOffsetFloat value1, 0.3333f$;
    final float value2$id$ = $getScaleOffsetFloat value2, 0.66666f$;
    final float gridSizeX$id$  = $getScaleOffsetFloat gridSizeX, 16$;
    final float gridSizeY$id$  = $getScaleOffsetFloat gridSizeY, 16$;
    final float x$id$  = $getScaleOffsetFloat canvasX, 0f$;
    final float y$id$  = $getScaleOffsetFloat canvasY, 0f$;

    final boolean onBandX$id$ = (gridSizeX$id$ == 0) ? false : ((int)(x$id$ / gridSizeX$id$)) % 2 == 0;
    final boolean onBandY$id$ = (gridSizeY$id$ == 0) ? false : ((int)(y$id$ / gridSizeY$id$)) % 2 == 0;
    final float value$id$ = (onBandX$id$ != onBandY$id$) ? value1$id$ : value2$id$;

    $setScaleOffsetFloat result$ value$id$;
  """) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val valueA = getScaleOffsetVar( "valueA", 0.3333f, variables, variableNameMappings )
    val valueB = getScaleOffsetVar( "valueB", 0.6666f, variables, variableNameMappings )
    val gridSize = getScaleOffsetVar( "gridSize", 16f, variables, variableNameMappings )
    val gridSizeX = getScaleOffsetVar( "gridSizeX", gridSize, variables, variableNameMappings )
    val gridSizeY = getScaleOffsetVar( "gridSizeY", gridSize, variables, variableNameMappings )
    val x = getScaleOffsetVar( "canvasX", 0f, variables, variableNameMappings )
    val y = getScaleOffsetVar( "canvasY", 0f, variables, variableNameMappings )

    def onBand(c: Float, size : Float): Boolean = if (size == 0) false else ((c / size).toInt) % 2 == 0
    val value = if (onBand(x, gridSizeX) != onBand(y, gridSizeY)) valueA else valueB

    setScaleOffsetVar( "result", value, variables, variableNameMappings )
  }
}