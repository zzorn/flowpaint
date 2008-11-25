package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import _root_.scala.collection.jcl.ArrayList
import brush.Brush
import javax.swing.JPanel

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionUi(brushes : ArrayList[Brush]) extends JPanel{

  def brushPreviewStrokeGenerator(f:Float, w:Float, h:Float, dataSample:DataSample) {

    val pressure = 0.5f + 0.5f*Math.cos( 2*Math.Pi * f + Math.Pi ).toFloat

    dataSample.setProperty("x", w * f)
    dataSample.setProperty("y", h * f)
    dataSample.setProperty("pressure", pressure)
    dataSample.setProperty("time", f * 0.5f)
  }

  setLayout( new java.awt.GridLayout( 0,1 ) )

  brushes.foreach( brush => add( new BrushPreview(brush, brushPreviewStrokeGenerator ) ) )
}