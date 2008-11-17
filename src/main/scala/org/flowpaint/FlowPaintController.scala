package org.flowpaint


import brush._
import filters.{ RadiusFromPressureFilter, StrokeFilter}
import input.PenInputHandler
import model.{Stroke, Painting}
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
  var currentBrush : Brush = new Brush( new GradientTestInk(), List( new StrokeAngleTilter(), new RadiusFromPressureFilter( 20 ) ) )
//  var currentRadius = 15f
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
/*
      sample.setProperty( "maxRadius", currentRadius )
*/
      sample.setProperty( "angle", currentAngle)
    }


  def quit() {
    System.exit(0)
  }

  def clearPicture() {
    currentPainting.clear()
    surface.clear()
    paintPanel.repaint()
  }

  def showAbout() {
    // TODO
  }

  def quickSave() {
    // TODO
  }

  def exportAs() {
    // TODO
  }

  def reportIssue() {
    // TODO
  }

  def goToHomePage() {
    // TODO
  }
}