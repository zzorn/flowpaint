package org.flowpaint

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, BorderLayout}
import javax.swing._
import model.{Stroke, Painting}
import renderer.SingleRenderSurface
import util.DataSample

/**
 * Main entrypoint for FlowPaint.
 *
 */
object FlowPaint {

  val sizeX = 1000
  val sizeY = 700

  val APPLICATION_NAME = "FlowPaint"
  val VERSION = "0.1"
  val VERSION_DESC = "Alpha"

  def main(args: Array[String])  {
    
    println( "FlowPaint started." )

    // Init controller
    val controller = new FlowPaintController()

    // Create UI Frame
    val frame = createUi( controller )
    frame.show
  }

  def createUi( controller : FlowPaintController ) : JFrame ={

    val frame = new JFrame( APPLICATION_NAME +" "+ VERSION +" "+VERSION_DESC )
    val mainPanel: JComponent = new JPanel(new BorderLayout())
    frame.setContentPane( mainPanel)
    mainPanel.setPreferredSize(new Dimension(sizeX, sizeY))
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    frame.setJMenuBar( createMenu(controller) )

    val toolBar = createToolBar(controller)
    toolBar.setFloatable( false );
    mainPanel.add( toolBar, BorderLayout.NORTH );

    mainPanel.add( createMainView(controller), BorderLayout.CENTER );

    frame.pack
    frame
  }

  def createToolBar(controller : FlowPaintController ):JToolBar={
    val toolbar = new JToolBar()

    toolbar.add( new AbstractAction("Quick Save") {
      def actionPerformed(e : java.awt.event.ActionEvent ) {
        controller.quickSave()
      }

    })

    toolbar.add( new AbstractAction("Quick Save and Clear Picture") {
      def actionPerformed(e : java.awt.event.ActionEvent ) {
        controller.quickSave()
        controller.clearPicture()
      }

    })

    toolbar.add( new AbstractAction("Clear Picture") {
      def actionPerformed(e : java.awt.event.ActionEvent ) {
        controller.clearPicture()
      }

    })


    toolbar
  }

  def createMenu(controller : FlowPaintController ):JMenuBar ={

    val menuBar = new JMenuBar()
    val fileMenu = new JMenu("File")
    menuBar.add( fileMenu )

    addMenuItem( fileMenu, "Quick Save", "Saves the current picture with a default filename",controller.quickSave )
    addMenuItem( fileMenu, "Export As...", "Saves the current picture with an user specified filename",controller.exportAs )
    fileMenu.add( new JSeparator() )
    addMenuItem( fileMenu, "Exit", "Closes the program immediately",controller.quit )

    val pictureMenu = new JMenu("Picture")
    menuBar.add( pictureMenu )

    addMenuItem( pictureMenu, "Clear", "Clears the picture",controller.clearPicture )

    val helpMenu = new JMenu("Help")
    menuBar.add( helpMenu )

    addMenuItem( helpMenu, "Go to FlowPaint Homepage", "Go to the FlowPaint project homepage (opens in browser)", controller.goToHomePage )
    addMenuItem( helpMenu, "Report Issue", "Report a bug or file a feature request (opens in browser)", controller.reportIssue)
    helpMenu.add( new JSeparator() )
    addMenuItem( helpMenu, "About FlowPaint", "Information about this paint program", controller.showAbout )

    menuBar
  }

  def addMenuItem(menu:JMenu, name:String, tooltip:String, action :()=>Unit) {
    val item = new JMenuItem(name)
    item.setToolTipText(tooltip )
    item.addActionListener( new ActionListener(){
      def actionPerformed( event :ActionEvent ) {
        action()
      }

    })
    menu.add( item )
  }

  def createMainView(controller : FlowPaintController ) : JComponent={

    controller.paintPanel

  }


}
