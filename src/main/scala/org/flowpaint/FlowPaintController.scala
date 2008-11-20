package org.flowpaint


import _root_.scala.collection.jcl.ArrayList
import brush._
import edu.stanford.ejalbert.BrowserLauncher
import filters.{RadiusFromPressureFilter, ZeroLengthSegmentFilter, StrokeFilter}
import gradient.{MultiGradient, Gradient, TwoColorGradient, GradientPoint}
import ink.{GradientInk, NoiseInk, Ink}
import input.PenInputHandler
import java.awt.Font
import model.{Stroke, Painting}
import renderer.{SingleRenderSurface, RenderSurface}
import tools.{StrokeTool, Tool}
import util.DataSample

/**
 *    Provides common methods of the application for various tools etc.
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

    def sampleFromColor(r: Float, g: Float, b: Float, a: Float): DataSample = {

      val data = new DataSample()
      data.setProperty("red", r)
      data.setProperty("green", g)
      data.setProperty("blue", b)
      data.setProperty("alpha", a)
      data
    }
    val twoColorGradient = new TwoColorGradient(sampleFromColor(0, 0, 0.5f, 1), sampleFromColor(0, 0.5f, 1, 0.1f))

    def makeGradientPoint(position: Float, r: Float, g: Float, b: Float, a: Float): GradientPoint = {

      GradientPoint(position, sampleFromColor(r, g, b, a))
    }
    val smoothTransparentBlackTransparentGradient = new MultiGradient(
      makeGradientPoint(0, 0, 0, 0, 0),
      makeGradientPoint(0.1f, 0, 0, 0, 0.01f),
      makeGradientPoint(0.2f, 0, 0, 0, 0.03f),
      makeGradientPoint(0.3f, 0, 0, 0, 0.04f),
      makeGradientPoint(0.4f, 0, 0, 0, 0.05f),
      makeGradientPoint(0.6f, 0, 0, 0, 0.05f),
      makeGradientPoint(0.7f, 0, 0, 0, 0.04f),
      makeGradientPoint(0.8f, 0, 0, 0, 0.03f),
      makeGradientPoint(0.9f, 0, 0, 0, 0.01f),
      makeGradientPoint(1, 0, 0, 0, 0))

    def makeColoredGradient( r:Float, g:Float, b:Float ) : Gradient = {

      new MultiGradient(
        makeGradientPoint(0.0f, r*0.7f, g*0.7f, b*0.3f, 0.0f),
        makeGradientPoint(0.2f, r*0.9f, g*0.8f, b*0.6f, 0.1f),
        makeGradientPoint(0.4f, r*1.0f, g*1.0f, b*0.7f, 0.4f),
        makeGradientPoint(0.5f, r*1.0f, g*1.0f, b*0.8f, 0.5f),
        makeGradientPoint(0.6f, r*1.0f, g*1.0f, b*0.7f, 0.4f),
        makeGradientPoint(0.8f, r*0.9f, g*0.8f, b*0.6f, 0.1f),
        makeGradientPoint(1.0f, r*0.7f, g*0.7f, b*0.3f, 0.0f))
    }


    val sepiaPenGradient = makeColoredGradient( 0.4f,0.25f,0.1f )
    val maroonPenGradient = makeColoredGradient( 0.6f,0.1f,0.2f )
    val ocraPenGradient = makeColoredGradient( 0.9f,0.7f,0.2f )
    val sapGreenPenGradient = makeColoredGradient( 0.3f,0.6f,0.1f )
    val purplePenGradient = makeColoredGradient( 0.3f,0.1f,0.5f )

    val grey = 0.35f
    val blackGradient = new MultiGradient(
      makeGradientPoint(0.0f, grey, grey, grey, 0.0f),
//      makeGradientPoint(0.1f, grey, grey, grey, 0.05f),
      makeGradientPoint(0.5f, grey, grey, grey, 0.4f),
//      makeGradientPoint(0.9f, grey, grey, grey, 0.05f),
      makeGradientPoint(1.0f, grey, grey, grey, 0.0f) )

    val inkGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.2f, 0, 0.3f, 0.5f),
      makeGradientPoint(0.1f, 0, 0, 0.2f, 0.8f),
      makeGradientPoint(0.5f, 0, 0, 0, 0.9f),
      makeGradientPoint(0.9f, 0, 0, 0.2f, 0.8f),
      makeGradientPoint(1.0f, 0.1f, 0, 0.4f, 0.5f) )

    val a = 0.8f
    val b = 0.6f
    val c = 0.3f
    val marbleGradient = new MultiGradient(
      makeGradientPoint(0.0f, c, c, c, 0.5f),
      makeGradientPoint(0.2f, a, a, a, 0.3f),
      makeGradientPoint(0.4f, c, c, c, 0.5f),
      makeGradientPoint(0.6f, c, c, c, 0.5f),
      makeGradientPoint(0.8f, b, b, b, 0.3f),
      makeGradientPoint(1.0f, c, c, c, 0.5f) )

    val sunflowerGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1.0f, 1.0f, 0.2f, 1),
      makeGradientPoint(0.5f, 1.0f, 0.8f, 0.1f, 1),
      makeGradientPoint(0.9f, 0.9f, 0.5f, 0.0f, 1),
      makeGradientPoint(1.0f, 0.7f, 0.3f, 0.0f, 1)
      )

    val fireGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1.0f, 0.9f, 0.2f, 1),
      makeGradientPoint(0.3f, 1.0f, 0.7f, 0.0f, 1),
      makeGradientPoint(0.4f, 0.8f, 0.2f, 0.0f, 1f),
      makeGradientPoint(0.5f, 0.6f, 0.1f, 0.0f, 0.8f),
      makeGradientPoint(0.6f, 0.4f, 0f, 0.0f, 0f),
      makeGradientPoint(1.0f, 0, 0, 0, 0))

    val skyCloudGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0f, 0.2f, 0.7f, 1),
      makeGradientPoint(0.7f, 0f, 0.2f, 0.7f, 1),
      makeGradientPoint(0.72f, 0.5f, 0.5f, 0.9f, 1),
      makeGradientPoint(0.75f, 1f, 1f, 1.0f, 1),
      makeGradientPoint(1.0f, 0.85f, 0.85f, 0.9f, 1)
    )

    val alienGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.4f, 0.9f, 0.0f, 0f),
      makeGradientPoint(0.3f, 0.3f, 0.8f, 0.1f, 0.7f),
      makeGradientPoint(0.5f, 0.2f, 0.7f, 0.3f, 1),
      makeGradientPoint(0.7f, 0.1f, 0.6f, 0.3f, 0.7f),
      makeGradientPoint(1.0f, 0.0f, 0.0f, 0.2f, 0f)
    )

    def addBrush(ink: Ink,radius: Float, tilt :Float): Brush = {
      val brush = new Brush(ink,
        List(new ZeroLengthSegmentFilter(), new StrokeAngleTilter(tilt), new RadiusFromPressureFilter(radius)))
      availableBrushes.add(brush)
      brush
    }

    currentBrush = addBrush(new GradientInk(blackGradient), 3,0)
    addBrush(new GradientInk(inkGradient), 6, 0.8f)

    addBrush(new GradientTestInk(0f, 1f), 10,0)
    addBrush(new GradientTestInk(0.5f, 1f), 30,0)
    addBrush(new GradientTestInk(1f, 1f), 80,0)

    addBrush(new GradientInk(sepiaPenGradient), 15,0)
    addBrush(new GradientInk(maroonPenGradient), 15,0)
    addBrush(new GradientInk(ocraPenGradient), 15,0)
    addBrush(new GradientInk(sapGreenPenGradient), 15,0)
    addBrush(new GradientInk(purplePenGradient), 15,0)

    addBrush(new NoiseInk(sepiaPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(maroonPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(ocraPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(sapGreenPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(purplePenGradient, (77f, 2f)), 30,0)

    addBrush(new NoiseInk(twoColorGradient, (14f, 1.4f)), 40,0)

    addBrush(new GradientInk(smoothTransparentBlackTransparentGradient), 60,0)

    addBrush(new NoiseInk(sunflowerGradient, (15f, 4f)), 50,0.2f)
    addBrush(new NoiseInk(fireGradient, (20f, 0.5f)), 30,0)
    addBrush(new NoiseInk(skyCloudGradient, (10f, 0.2f)), 150,0.1f)
    addBrush(new NoiseInk(alienGradient, (40f, 2f)), 20,0)
    addBrush(new NoiseInk(marbleGradient , (80f, 5f)), 80,0)

    // State / datamodel info
    currentTool = new StrokeTool()
    currentPainting = new Painting()


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