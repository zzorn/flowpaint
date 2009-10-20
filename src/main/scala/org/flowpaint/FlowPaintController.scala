package org.flowpaint


import scala.collection.jcl.ArrayList
import scala.io.Source
import scala.xml.{Elem, PrettyPrinter}
import org.flowpaint.brush._
import edu.stanford.ejalbert.BrowserLauncher
import org.flowpaint.filters._
import org.flowpaint.gradient.{MultiGradient, Gradient, GradientPoint}
import org.flowpaint.ink._
import input.{InputHandler}

import java.awt.Font
import java.io.{File, BufferedWriter, Writer, FileWriter}

import javax.swing.{JFileChooser, SwingUtilities}
import org.flowpaint.model.{Stroke, Painting, Path, Layer}
import org.flowpaint.property.{BrushSliderEditor, GradientSliderEditor}
import org.flowpaint.renderer.{SingleRenderSurface, RenderSurface}
import org.flowpaint.tools.{StrokeTool, Tool}
import org.flowpaint.util._

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

      if (myCurrentBrush != null) {
        myCurrentBrush.removeChangeListener(zeroBrushUsage)
      }

      myCurrentBrush = brush.createCopy()

      myCurrentBrush.addChangeListener(zeroBrushUsage)
      zeroBrushUsage(myCurrentBrush)

      FlowPaintUi.brushUi.setBrush(myCurrentBrush)
    }

  }

  private def zeroBrushUsage(brush: Brush) {
    currentBrushUsageCount = 0
  }

  private val MAX_RECENT_BRUSHES_SIZE = 18

  private val UNDO_QUEUE_SIZE = 8

  var brushSets: List[BrushSet] = Nil

  val recentBrushes = new FixedBrushSet("recentBrushes", "Recent Brushes", MAX_RECENT_BRUSHES_SIZE, Nil)

  private var currentBrushUsageCount = 0;
  def addRecentBrush(brush: Brush) {
    currentBrushUsageCount += 1

    // Add only when the brush is used the second time
    if (currentBrushUsageCount == 2) {
      // NOTE: This causes some corruption of the next brush stroke sometimes, fix threading design.
      recentBrushes.addOrMoveBrushFirst(brush.createCopy())
    }
  }

  // Render cache bitmap
  var surface: SingleRenderSurface = null

  val commandQueue = new CommandQueue()

  // Rendering UI
  var paintPanel: PaintPanel = null

  // Input source
  var penInput: InputHandler = null

  var penManager: jpen.PenManager = null


  def start() {

    // Create default brush sets
    loadDefaults

    // Recent brushes set
    brushSets = brushSets ::: List(recentBrushes)

    // State / datamodel info
    currentTool = new StrokeTool()
    currentPainting = new Painting()


    // Render cache bitmap
    surface = new SingleRenderSurface(currentPainting, UNDO_QUEUE_SIZE)

    // Rendering UI
    paintPanel = new PaintPanel(surface, true)

    // Input source
    penInput = new InputHandler((sample: DataSample) => {
      currentTool.onEvent(sample)
      paintPanel.repaintChanges()
    })

    penManager = new jpen.PenManager(paintPanel)

    penManager.pen.addListener(penInput)

    FlowPaintUi.init()

    FlowPaintUi.frame.show
  }


  def loadDefaults() {

    val defaultFlowpaintData: Elem = ResourceLoader.loadXml("default-brushes.xml", "default FlowPaint settings", <flowpaint/>)

    FlowPaint.library.fromXML(defaultFlowpaintData)

    brushSets = FlowPaint.library.getTomes(classOf[BrushSet])
    currentBrush = if (!brushSets.isEmpty && !brushSets(0).getBrushes().isEmpty) brushSets(0).getBrushes()(0) else null

    if (FlowPaintUi.frame != null) FlowPaintUi.frame.validate
  }

  def quit() {
    System.exit(0)
  }

  def clearPicture() {
    clearPictureWithoutMessage()
    FlowPaintUi.showMessage("Picture Cleared")
  }


  def clearPictureWithoutMessage() {
    doUndoableClear()
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


  def storeStroke(stroke: Stroke) {
    commandQueue.queueCommand(new Command(
      "Brush Stroke",
      () => {
        //surface.undoSnapshot()
        // TODO: Make undo stacks document specific
        FlowPaintController.currentPainting.currentLayer.addStroke(stroke)
        //stroke.updateSurface( surface )
        paintPanel.repaint()
        stroke
      },
      (undoData: Object) => {
        val undoedStroke = undoData.asInstanceOf[Stroke]
        FlowPaintController.currentPainting.currentLayer.removeStroke(undoedStroke)
        surface.undo()
        paintPanel.repaintChanges()
        undoedStroke
      },
      (redoData: Object) => {
        val stroke = redoData.asInstanceOf[Stroke]
        FlowPaintController.currentPainting.currentLayer.addStroke(stroke)
        stroke.updateSurface(surface)
        paintPanel.repaint()
        stroke
      },
      () => {
        surface.canUndo
      }))
  }

  // TODO: Make undo stacks document (= painting or tome) specific.
  private def doUndoableClear() {
    commandQueue.queueCommand(new Command(
      "Clear",
      () => {
        surface.undoSnapshot()

        val layers = currentPainting.getLayers
        currentPainting.clear()
        surface.clear()
        paintPanel.repaint()

        layers
      },
      (undoData: Object) => {

        val layers = undoData.asInstanceOf[List[Layer]]
        currentPainting.setLayers(layers)
        surface.undo()
        paintPanel.repaint()

        null
      },
      (redoData: Object) => {
        surface.undoSnapshot()

        val layers = currentPainting.getLayers
        currentPainting.clear()
        surface.clear()
        paintPanel.repaint()

        layers
      },
      () => {
        surface.canUndo
      }))
  }

  def canUndo() = commandQueue.canUndo && surface.canUndo

  def undo() = commandQueue.undo

  def canRedo() = commandQueue.canRedo

  def redo() = commandQueue.redo


  def exportRecentBrushes() {

    def createXmlExport() = {
      val pp = new PrettyPrinter(120, 3)
      val exportedBrushes: List[String] = recentBrushes.getBrushes map { (brush : Brush) => pp.format(brush.toXML())}
      exportedBrushes.mkString("\n\n")
    }

    def saveText( file : File, text : String ) {
      val writer = new BufferedWriter(new FileWriter( file ))
      try {
        writer.write( text )
      }
      finally {
        writer.close()
      }
    }

    val fc = new JFileChooser()
    val returnVal = fc.showSaveDialog(FlowPaintUi.frame)

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      val file = fc.getSelectedFile()
      
      saveText( file, createXmlExport() )

      // TODO: Add exception handling, show error message if save failed
      
      FlowPaintUi.showMessage( "Exported recent brushes to " + file.getPath )
    }
  }


}