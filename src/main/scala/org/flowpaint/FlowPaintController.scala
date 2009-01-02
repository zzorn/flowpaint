package org.flowpaint


import _root_.scala.collection.jcl.ArrayList
import brush._
import edu.stanford.ejalbert.BrowserLauncher
import filters._
import gradient.{MultiGradient, Gradient,  GradientPoint}
import ink._
import input.{InputHandler}

import java.awt.Font
import javax.swing.SwingUtilities
import model.{Stroke, Painting}
import property.{BrushSliderEditor, GradientSliderEditor}
import renderer.{SingleRenderSurface, RenderSurface}
import tools.{StrokeTool, Tool}
import util.{DataSample, LibraryImpl, PropertyRegister}
/**
 *         Provides common methods of the application for various tools etc.
 *
 * @author Hans Haggstrom
 */
// TODO: Extract the default brush setup code..
object FlowPaintController {


  // State / datamodel info
  var currentTool: Tool = null
  var currentPainting: Painting = null


  private var myCurrentBrush: Brush = null

  def currentBrush = myCurrentBrush

  def currentBrush_=(brush: Brush) {


    if (myCurrentBrush != brush) {

      if (myCurrentBrush != null){
        myCurrentBrush.removeChangeListener( zeroBrushUsage )
      }

      myCurrentBrush = brush.createCopy()

      myCurrentBrush.addChangeListener( zeroBrushUsage )
      zeroBrushUsage( myCurrentBrush )

      FlowPaintUi.brushUi.setBrush(myCurrentBrush)
    }

  }

  private def zeroBrushUsage( brush : Brush ) {
    currentBrushUsageCount = 0
  }

  private val MAX_RECENT_BRUSHES_SIZE = 24

  var brushSets :List[BrushSet] = Nil

  val recentBrushes = new FixedBrushSet( "recentBrushes", "Recent Brushes", MAX_RECENT_BRUSHES_SIZE, Nil )

  private var currentBrushUsageCount = 0;
  def addRecentBrush(brush: Brush) {
    currentBrushUsageCount += 1

    // Add only when the brush is used the second time
    if (currentBrushUsageCount == 2) {
      // NOTE: This causes some corruption of the next brush stroke sometimes, fix threading design.
      recentBrushes.addOrMoveBrushFirst( brush.createCopy() )
    }
  }

  // Render cache bitmap
  var surface: SingleRenderSurface = null

  // Rendering UI
  var paintPanel: PaintPanel = null

  // Input source
  var penInput: InputHandler = null

  var penManager: jpen.PenManager = null


  def start() {

    // Create default brush sets
    var (sets, current) = DefaultBrushFactory.createDefaultBrushes
    brushSets = sets
    currentBrush = current

    // Recent brushes set
    brushSets = brushSets ::: List( recentBrushes )
    
    // State / datamodel info
    currentTool = new StrokeTool()
    currentPainting = new Painting()


    // Render cache bitmap
    surface = new SingleRenderSurface(currentPainting)

    // Rendering UI
    paintPanel = new PaintPanel(surface, true)

    // Input source
    penInput = new InputHandler((sample: DataSample) => {
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
    util.InternetUtils.openUrlInBrowser(FlowPaint.BUG_REPORT_URL, "issue report page")
  }

  def requestFeature() {
    util.InternetUtils.openUrlInBrowser(FlowPaint.FEATURE_REQUEST_URL, "feature request page")
  }

  def goToHomePage() {
    util.InternetUtils.openUrlInBrowser(FlowPaint.HOMEPAGE_URL, FlowPaint.APPLICATION_NAME + " homepage")
  }

  def showAbout() {

    val optionPane = new javax.swing.JOptionPane()
    optionPane.setMessage(FlowPaint.ABOUT);
    optionPane.setMessageType(javax.swing.JOptionPane.PLAIN_MESSAGE);
    /* Let's wait until we get a proper icon, and fix the layout of the about dialog too.
        optionPane.setIcon(FlowPaint.APPLICATION_ICON)
    */
    val dialog = optionPane.createDialog(FlowPaintUi.frame, FlowPaint.NAME_AND_VERSION);
    dialog.setVisible(true);
  }


}