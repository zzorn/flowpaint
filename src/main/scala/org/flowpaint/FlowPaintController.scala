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

    def createSmoothGradient( f: Float, v : Float ) : Gradient = {

      new MultiGradient(
        makeGradientPoint(0.0f, v, v, v, 0),
        makeGradientPoint(0.1f, v, v, v, 0.1f * f),
        makeGradientPoint(0.2f, v, v, v, 0.3f * f),
        makeGradientPoint(0.3f, v, v, v, 0.4f * f),
        makeGradientPoint(0.4f, v, v, v, 0.5f * f),
        makeGradientPoint(0.6f, v, v, v, 0.5f * f),
        makeGradientPoint(0.7f, v, v, v, 0.4f * f),
        makeGradientPoint(0.8f, v, v, v, 0.3f * f),
        makeGradientPoint(0.9f, v, v, v, 0.1f * f),
        makeGradientPoint(1.0f, v, v, v, 0))
    }

    def makeColoredGradient( r:Float, g:Float, b:Float, a:Float ) : Gradient = {

      new MultiGradient(
        makeGradientPoint(0.0f, r*0.9f, g*0.8f, b*0.3f, a*0.0f),
        makeGradientPoint(0.1f, r*0.95f, g*0.9f, b*0.6f, a*0.3f),
        makeGradientPoint(0.3f, r*1.0f, g*1.0f, b*0.7f, a*0.85f),
        makeGradientPoint(0.5f, r*1.0f, g*1.0f, b*0.8f, a*1f),
        makeGradientPoint(0.7f, r*1.0f, g*1.0f, b*0.7f, a*0.85f),
        makeGradientPoint(0.9f, r*0.95f, g*0.9f, b*0.6f, a*0.3f),
        makeGradientPoint(1.0f, r*0.9f, g*0.8f, b*0.3f, a*0.0f))
    }


    val sepiaPenGradient = makeColoredGradient( 0.4f,0.25f,0.1f, 0.45f )
    val maroonPenGradient = makeColoredGradient( 0.6f,0.1f,0.2f, 0.45f )
    val ocraPenGradient = makeColoredGradient( 0.9f,0.7f,0.2f, 0.45f )
    val sapGreenPenGradient = makeColoredGradient( 0.3f,0.6f,0.1f, 0.45f )
    val purplePenGradient = makeColoredGradient( 0.2f,0.1f,0.6f, 0.45f )
    val lampBlackPenGradient = makeColoredGradient( 0.03f,0.08f,0.15f, 0.45f )

    val grey = 0.35f
    val blackGradient = new MultiGradient(
      makeGradientPoint(0.0f, grey, grey, grey, 0.0f),
//      makeGradientPoint(0.1f, grey, grey, grey, 0.05f),
      makeGradientPoint(0.5f, grey, grey, grey, 0.6f),
//      makeGradientPoint(0.9f, grey, grey, grey, 0.05f),
      makeGradientPoint(1.0f, grey, grey, grey, 0.0f) )

    val inkGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.15f, 0, 0.2f, 0.5f),
      makeGradientPoint(0.1f, 0, 0, 0.13f, 0.8f),
      makeGradientPoint(0.5f, 0, 0, 0, 0.9f),
      makeGradientPoint(0.9f, 0, 0, 0.13f, 0.8f),
      makeGradientPoint(1.0f, 0.1f, 0, 0.25f, 0.5f) )

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

    val woodGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.4f, 0.25f, 0.15f, 1),
      makeGradientPoint(0.3f, 0.5f, 0.3f, 0.2f, 1),
      makeGradientPoint(0.31f, 0.7f, 0.6f, 0.25f, 1),
      makeGradientPoint(0.5f, 0.6f, 0.3f, 0.2f, 1),
      makeGradientPoint(0.53f, 0.75f, 0.6f, 0.3f, 1),
      makeGradientPoint(0.8f, 0.5f, 0.35f, 0.25f, 1),
      makeGradientPoint(0.82f, 0.7f, 0.6f, 0.25f, 1),
      makeGradientPoint(1.0f, 0.5f, 0.3f, 0.2f, 1)
      )

    val fireGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1.0f, 0.7f, 0.1f, 1),
      makeGradientPoint(0.2f, 1.0f, 0.4f, 0.0f, 1),
      makeGradientPoint(0.5f, 0.8f, 0.2f, 0.0f, 1f),
      makeGradientPoint(0.6f, 0.6f, 0.1f, 0.0f, 0.8f),
      makeGradientPoint(0.7f, 0.4f, 0f, 0.0f, 0f),
      makeGradientPoint(1.0f, 0, 0, 0, 0))

    val skyCloudGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0f, 0.15f, 0.65f, 0),
      makeGradientPoint(0.4f, 0f, 0.15f, 0.65f, 0),
      makeGradientPoint(0.60f, 0f, 0.15f, 0.65f, 0.1f),
      makeGradientPoint(0.63f, 0.3f, 0.5f, 0.9f, 0.9f),
      makeGradientPoint(0.68f, 1f, 1f, 1.0f, 1),
      makeGradientPoint(1.0f, 0.6f, 0.6f, 0.8f, 1)
    )

    val alienGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.1f, 0.4f, 0.2f, 1),
      makeGradientPoint(0.1f, 0.3f, 0.5f, 0.1f, 1f),
      makeGradientPoint(0.2f, 0.0f, 0.3f, 0.22f, 1f),
      makeGradientPoint(0.8f, 0.2f, 0.6f, 0.15f, 1f),
      makeGradientPoint(0.85f, 0.05f, 0.15f, 0.15f, 1f),
      makeGradientPoint(1.0f, 0.0f, 0.1f, 0.15f, 1f)
    )

    val solidBlackGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0,0,0, 0),
      makeGradientPoint(0.5f, 0,0,0, 1),
      makeGradientPoint(1.0f, 0,0,0, 0)
    )

    val whiteGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1,1,1, 0f),
      makeGradientPoint(0.2f, 1,1,1, 0.2f),
      makeGradientPoint(0.4f, 1,1,1, 1f),
      makeGradientPoint(0.6f, 1,1,1, 1f),
      makeGradientPoint(0.8f, 1,1,1, 0.2f),
      makeGradientPoint(1.0f, 1,1,1, 0f)
    )

    def addBrush(ink: Ink,radius: Float, tilt :Float, pressureEffectOnRadius : Float): Brush = {
      val brush = new Brush(ink,
        List(new ZeroLengthSegmentFilter(), new StrokeAngleTilter(tilt), new RadiusFromPressureFilter(radius, pressureEffectOnRadius)))
      availableBrushes.add(brush)
      brush
    }

    currentBrush = addBrush(new GradientInk(blackGradient, 1), 2.5f,0,0.35f)
    addBrush(new GradientInk(solidBlackGradient, 0), 1, 0,0)
    addBrush(new GradientInk(inkGradient, 0), 2.7f, 0.8f,1)
    addBrush(new GradientInk(inkGradient, 0), 8, 0.8f,1)
    addBrush(new GradientInk(createSmoothGradient(0.24f, 0.26f), 1f), 23,0,0.5f)
    addBrush(new GradientInk(createSmoothGradient(0.25f, 0), 1f), 70,0,0.5f)
    addBrush(new GradientInk(createSmoothGradient(0.8f, 1f), 1f), 30,0,0.5f)
    addBrush(new GradientInk(whiteGradient, 0), 45,0,1f)

    addBrush(new GradientInk(sepiaPenGradient, 0.7f), 20,0, 1f)
    addBrush(new GradientInk(maroonPenGradient, 0.7f), 20,0, 1f)
    addBrush(new GradientInk(ocraPenGradient, 0.7f), 20,0, 1f)
    addBrush(new GradientInk(sapGreenPenGradient, 0.7f), 20,0, 1f)
    addBrush(new GradientInk(purplePenGradient, 0.7f), 20,0, 1f)
    addBrush(new GradientInk(lampBlackPenGradient, 0.7f ), 20,0, 1f)

/*
    addBrush(new NoiseInk(sepiaPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(maroonPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(ocraPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(sapGreenPenGradient, (77f, 2f)), 30,0)
    addBrush(new NoiseInk(purplePenGradient, (77f, 2f)), 30,0)
*/

    addBrush(new NoiseInk(woodGradient, (15f, 2.1f), 0.2f), 30,0, 0.9f)
    addBrush(new NoiseInk(fireGradient, (30f, 1f), 0.35f), 18,0, 1f)
    addBrush(new NoiseInk(sunflowerGradient, (15f, 2f), 0.2f), 20,0.2f, 1)
    addBrush(new NoiseInk(alienGradient, (3f, 0.5f), 0.4f), 10,0.3f, 1f)
    addBrush(new NoiseInk(twoColorGradient, (14f, 1.4f), 1f), 40,0,1)
    addBrush(new NoiseInk(marbleGradient , (50f, 1.7f), 0.3f), 40,0, 0.7f)

    addBrush(new GradientTestInk(0f, 1f), 10,0,1)
    addBrush(new GradientTestInk(0.5f, 1f), 30,0,1)
    addBrush(new GradientTestInk(1f, 1f), 80,0,1)

    addBrush(new NoiseInk(skyCloudGradient, (2f, 0.6f), 0.5f), 30,0,1f)
    addBrush(new NoiseInk(skyCloudGradient, (50f, 1.5f), 0.8f), 50,0,0.5f)

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