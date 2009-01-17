package org.flowpaint.pixelprocessors
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.{DataSample, MathUtils}
/**
 * 
 *
 * @author Hans Haggstrom
 */

class InterpolateSmoothly extends PixelProcessor("","",
  """
    final float input$id$        = $getScaleOffsetFloat input, 0f$;
    final float inputStart$id$   = $getScaleOffsetFloat inputStart, 0f$;
    final float inputEnd$id$     = $getScaleOffsetFloat inputEnd, 1f$;
    final float outputStart$id$  = $getScaleOffsetFloat outputStart, 0f$;
    final float outputEnd$id$    = $getScaleOffsetFloat outputEnd, 1f$;

    // Check for special case where start and end positions are the same.  In this case return the average value.
    if ( inputStart$id$ == inputEnd$id$ )
    {
        return 0.5f * ( outputStart$id$ + outputEnd$id$ );
    }

    final float relativePosition$id$ =  ( input$id$ - inputStart$id$ ) / ( inputEnd$id$ - inputStart$id$ );
    final float t$id$ = ((float)(1f - Math.cos(relativePosition$id$ * Math.PI))) * 0.5f
    final float result$id$ = outputStart$id$ + t$id$ * ( outputEnd$id$ - outputStart$id$ );

    $setScaleOffsetFloat result$ result$id$;
  """ ) {

  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings : Data) {

    val input = getScaleOffsetVar( "input", 0.5f, variables, variableNameMappings )
    val inputStart = getScaleOffsetVar( "inputStart", 0f, variables, variableNameMappings )
    val inputEnd = getScaleOffsetVar( "inputEnd", 1f, variables, variableNameMappings )
    val outputStart = getScaleOffsetVar( "outputStart", 0f, variables, variableNameMappings )
    val outputEnd = getScaleOffsetVar( "outputEnd", 1f, variables, variableNameMappings )

    val result = MathUtils.interpolateSmoothly( input, inputStart, inputEnd, outputStart, outputEnd )

    setScaleOffsetVar( "result", result, variables, variableNameMappings )
  }
}