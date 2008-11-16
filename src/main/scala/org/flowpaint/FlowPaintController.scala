package org.flowpaint


import brush.{Brush, GradientTestBrush}
import input.PenInputHandler
import model.Painting
import renderer.SingleRenderSurface
import tools.StrokeTool
import util.DataSample

/**
 * Provides common methods of the application for various tools etc.
 *
 * @author Hans Haggstrom
 */
class FlowPaintController() {

  // State / datamodel info
  var currentTool = new StrokeTool()
  var currentPainting = new Painting()
  var currentBrush : Brush = new GradientTestBrush()
  var currentRadius = 30f
  var currentAngle = Math.toRadians( 90+45 ).toFloat

  // Render cache bitmap
  val surface = new SingleRenderSurface( currentPainting )

  // Rendering UI
  val paintPanel = new PaintPanel( surface )

  // Input source
  val penInput = new  PenInputHandler( (sample:DataSample) => {
    currentTool.onEvent( sample, this )
    paintPanel.repaint()
  } )

  val penManager = new jpen.PenManager( paintPanel )

  penManager.pen.addListener( penInput )




  def fillDataSampleWithCurrentSettings( sample : DataSample )
    {
      sample.setProperty( "radius", currentRadius )
      sample.setProperty( "angle", currentAngle)
    }


}