package org.flowpaint


import brush._
import edu.stanford.ejalbert.BrowserLauncher
import filters.{RadiusFromPressureFilter, ZeroLengthSegmentFilter, StrokeFilter}
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
  var currentAngle: Float = 0f


  // Render cache bitmap
  var surface: SingleRenderSurface = null

  // Rendering UI
  var paintPanel: PaintPanel = null

  // Input source
  var penInput: PenInputHandler = null

  var penManager: jpen.PenManager = null


  def start() {

    // State / datamodel info
    currentTool = new StrokeTool()
    currentPainting = new Painting()
    currentBrush = new Brush(new GradientTestInk(),
      List(new ZeroLengthSegmentFilter(), new StrokeAngleTilter(), new RadiusFromPressureFilter(30)))

    currentAngle = Math.toRadians(90 + 45).toFloat

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


  def fillDataSampleWithCurrentSettings(sample: DataSample)
    {
      /*
            sample.setProperty( "maxRadius", currentRadius )
      */
      sample.setProperty("angle", currentAngle)
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