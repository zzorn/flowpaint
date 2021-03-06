package org.flowpaint


import org.flowpaint.brush.BrushSet
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, BorderLayout}
import javax.swing._
import javax.swing.plaf.ColorUIResource
import java.awt.event.KeyEvent
import net.miginfocom.swing.MigLayout
import org.flowpaint.ui.{ParameterPanel, BrushSelectionUi, BrushUi}
/**
 *
 *
 * @author Hans Haggstrom
 */

object FlowPaintUi {
  val sizeX = 1000
  val sizeY = 700

  var frame: JFrame = null

  var status: JLabel = null

  val brushUi = new BrushUi()

  val SIDEBAR_WIDTH: Int = 192

  def init()
    {
      frame = createUi()
    }


  /**
   *  Shows a message to the user, by default in the statusbar.
   */
  def showMessage(message: String) {
    if (status != null) status.setText(message)
    else println("Message: " + message)
  }

  def createUi(): JFrame = {

    val frame = new JFrame(FlowPaint.NAME_AND_VERSION)

    // Globally use heavyweight components for all popup menus, as it seems they tend to garble up the main image for some reason.
    JPopupMenu.setDefaultLightWeightPopupEnabled(false);

    if (FlowPaint.APPLICATION_ICON != null) frame.setIconImage( FlowPaint.APPLICATION_ICON.getImage() )


    val mainPanel: JComponent = new JPanel(new BorderLayout())
    frame.setContentPane(mainPanel)
    mainPanel.setPreferredSize(new Dimension(sizeX, sizeY))
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    frame.setJMenuBar(createMenu())

    val toolBar = createToolBar()
    toolBar.setFloatable(false);
    mainPanel.add(toolBar, BorderLayout.NORTH)

    mainPanel.add(createMainView(), BorderLayout.CENTER)

    val brushParameterUi = brushUi.ui
    val sidebar = new JPanel( new MigLayout( "wrap 1, fillx, insets 0","[grow]","0[]4[]0" ))
    sidebar.setPreferredSize( new Dimension( SIDEBAR_WIDTH,5000 )  )
    FlowPaintController.brushSets foreach ( (brushSet : BrushSet) =>
            sidebar.add( new BrushSelectionUi(brushSet),"width 100%"  ))

    sidebar.add( brushParameterUi,"width 100%"  )
    mainPanel.add(sidebar, BorderLayout.EAST)
    

    status = new JLabel(" ")
    mainPanel.add(status, BorderLayout.SOUTH)

    frame.pack
    frame
  }

  def createToolBar(): JToolBar = {

    def addToolButton(toolbar: JToolBar, name: String, mnemonic: Int, action: () => Unit) {
      val toolAction = new AbstractAction(name) {
        def actionPerformed(e: java.awt.event.ActionEvent) {
          action()
        }

      }
      toolAction.putValue(Action.MNEMONIC_KEY, mnemonic)
      toolbar.add(toolAction)
    }

    val toolbar = new JToolBar()

    addToolButton(toolbar, "Quick Save", KeyEvent.VK_S, FlowPaintController.quickSave)
    addToolButton(toolbar, "Quick Save and Clear Picture", KeyEvent.VK_A, FlowPaintController.quickSaveAndClearPicture)
    addToolButton(toolbar, "Clear Picture", KeyEvent.VK_C, FlowPaintController.clearPicture)

    // TODO: Listen to change of picture
    toolbar.add( FlowPaintController.currentPicture.commandQueue.undoAction )

/* TODO: Surface needs redo support first
    toolbar.add( FlowPaintController.commandQueue.redoAction )
*/

    toolbar
  }


  def createMenu(): JMenuBar = {

    def addMenuItem(menu: JMenu, name: String, mnemonic: Int, tooltip: String, action: () => Unit) {
      val item = new JMenuItem(name)
      item.setToolTipText(tooltip)
      item.setMnemonic(mnemonic)
      item.addActionListener(new ActionListener() {
        def actionPerformed(event: ActionEvent) {
          action()
        }

      })
      menu.add(item)
    }

    val menuBar = new JMenuBar()
    val fileMenu = new JMenu("File")
    fileMenu.setMnemonic(KeyEvent.VK_F)
    menuBar.add(fileMenu)

    addMenuItem(fileMenu, "Quick Save", KeyEvent.VK_S, "Saves the current picture with a new default filename in the current directory.", FlowPaintController.quickSave)
    fileMenu.add(new JSeparator())
/* for development only
    addMenuItem(fileMenu, "Export Recent Brushes", KeyEvent.VK_R, "Saves the current recent brushes to the specified file, overwriting any earlier content.", FlowPaintController.exportRecentBrushes)
    fileMenu.add(new JSeparator())
*/
    addMenuItem(fileMenu, "Exit", KeyEvent.VK_X, "Closes the program immediately.  Unsaved changes will be lost.", FlowPaintController.quit)

    val pictureMenu = new JMenu("Picture")
    pictureMenu.setMnemonic(KeyEvent.VK_P)
    menuBar.add(pictureMenu)

    addMenuItem(pictureMenu, "Clear", KeyEvent.VK_C, "Clears the picture", FlowPaintController.clearPicture)

    // TODO: Listen to picture change
    pictureMenu .add( FlowPaintController.currentPicture.commandQueue.undoAction )

    val helpMenu = new JMenu("Help")
    helpMenu.setMnemonic(KeyEvent.VK_H)
    menuBar.add(helpMenu)

    addMenuItem(helpMenu, "Go to FlowPaint Homepage", KeyEvent.VK_H, "Go to the FlowPaint project homepage (opens in browser)", FlowPaintController.goToHomePage)
    addMenuItem(helpMenu, "Report Issue", KeyEvent.VK_R, "<html>Report a bug or file a feature request.  Opens in browser.<br/>Requires a Google account, as it uses the Google Code issue tracker.</html>", FlowPaintController.reportIssue)
    addMenuItem(helpMenu, "Request Feature", KeyEvent.VK_F, "<html>Report a bug or file a feature request.  Opens in browser.<br/>Requires a Google account, as it uses the Google Code issue tracker.</html>", FlowPaintController.requestFeature)
    helpMenu.add(new JSeparator())
    addMenuItem(helpMenu, "About FlowPaint", KeyEvent.VK_A, "Information about this paint program", FlowPaintController.showAbout)


    menuBar
  }


  def createMainView(): JComponent = {

    FlowPaintController.paintPanel

  }


}