package org.flowpaint


import _root_.scala.collection.jcl.ArrayList
import brush._
import edu.stanford.ejalbert.BrowserLauncher
import filters.{RadiusFromPressureFilter, ZeroLengthSegmentFilter, StrokeFilter}
import gradient.TwoColorGradient
import ink.NoiseInk
import input.PenInputHandler
import java.awt.Font
import model.{Stroke, Painting}
import renderer.{SingleRenderSurface, RenderSurface}
import tools.{StrokeTool, Tool}
import util.DataSample

/**
 *   Provides common methods of the application for various tools etc.
 *
 * @author Hans Haggstrom
 */
object FlowPaintController {

  // State / datamodel info
  var currentTool: Tool = null
  var currentPainting: Painting = null
  var currentBrush: Brush = null

  val availableBrushes = new ArrayList[Brush]()

  // Render cache bitmap
  var surface: SingleRenderSurface = null

  // Rendering UI
  var paintPanel: PaintPanel = null

  // Input source
  var penInput: PenInputHandler = null

  var penManager: jpen.PenManager = null


  def start() {

    def sampleFromColor(r:Float, g:Float, b:Float, a:Float):DataSample ={

      val data = new DataSample()
      data.setProperty("red",r)
      data.setProperty("green",g)
      data.setProperty("blue",b)
      data.setProperty("alpha",a)
      data
    }
    val twoColorGradient = new TwoColorGradient( sampleFromColor(0,0,0.5f,1), sampleFromColor(0,0.5f,1,0.1f) )


    val brush1 = new Brush(new GradientTestInk(0f, 0f),
      List(new ZeroLengthSegmentFilter(  ), new StrokeAngleTilter(), new RadiusFromPressureFilter(5)))
    val brush2 = new Brush(new NoiseInk( twoColorGradient, (14f,1.4f)),
      List(new ZeroLengthSegmentFilter(), new StrokeAngleTilter(), new RadiusFromPressureFilter(20)))
    val brush3 = new Brush(new GradientTestInk(0.5f, 0.5f),
      List(new ZeroLengthSegmentFilter(), new StrokeAngleTilter(), new RadiusFromPressureFilter(30)))
    val brush4 = new Brush(new GradientTestInk(1f, 1f),
      List(new ZeroLengthSegmentFilter(), new StrokeAngleTilter(), new RadiusFromPressureFilter(70)))

   // Init brush collection
    availableBrushes.add( brush1 )
    availableBrushes.add( brush2 )
    availableBrushes.add( brush3 )
    availableBrushes.add( brush4 )

    // State / datamodel info
    currentTool = new StrokeTool()
    currentPainting = new Painting()
    currentBrush = brush3


    // Render cache bitmap
    surface = new SingleRenderSurface(currentPainting)

    // Rendering UI
    paintPanel = new PaintPanel(surface)

    // Input source
    penInput = new PenInputHandler((sample: DataSample) => {
      currentTool.onEvent(sample)
      paintPanel.repaint()
    })

    penManager = new jpen.PenManager(paintPanel)

    penManager.pen.addListener(penInput)


    FlowPaintUi.init()


    FlowPaintUi.frame.show
  }




  def quit() {
    System.exit(0)
  }

  def clearPicture() {
    clearPictureWithoutMessage()
    FlowPaintUi.showMessage("Picture Cleared")
  }


  def clearPictureWithoutMessage() {
    currentPainting.clear()
    surface.clear()
    paintPanel.repaint()
  }

  def quickSave() {
    actions.QuickSave.invokeAction()
  }

  def quickSaveAndClearPicture() {
    val errorMessage = actions.QuickSave.quickSave()
    if (errorMessage != null) FlowPaintUi.showMessage(errorMessage)
    else clearPictureWithoutMessage()

  }

  def reportIssue() {
    val launcher = new BrowserLauncher()
    launcher.openURLinBrowser("http://code.google.com/p/flowpaint/issues/entry?template=Defect%20report%20from%20user");
  }

  def requestFeature() {
    val launcher = new BrowserLauncher()
    launcher.openURLinBrowser("http://code.google.com/p/flowpaint/issues/entry?template=Feature%20request%20from%20user");
  }

  def goToHomePage() {
    val launcher = new BrowserLauncher()
    launcher.openURLinBrowser("http://code.google.com/p/flowpaint");
  }

  def showAbout() {

    val optionPane = new javax.swing.JOptionPane()
    optionPane.setMessage(FlowPaint.ABOUT);
    optionPane.setMessageType(javax.swing.JOptionPane.PLAIN_MESSAGE);
    //   optionPane.setIcon()
    val dialog = optionPane.createDialog(FlowPaintUi.frame, FlowPaint.DESCRIPTION);
    dialog.setVisible(true);
  }


}