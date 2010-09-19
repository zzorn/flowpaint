package org.flowpaint.ui.editors


import _root_.org.flowpaint.property.Data
import javax.swing.{JPanel, JComponent}
import org.flowpaint.util.Configuration

/**
 * Something that can edit some properties of a Data.
 *
 * @author Hans Haggstrom
 */
abstract class Editor extends Configuration {

  private var currentEditedData : Data = null
  private var currentUi : JComponent = null

  def updateUi( changedData : Data, changedProperty : String ) {

    onEditedDataChanged(changedProperty )

    if ( currentUi != null) currentUi.repaint()
  }

  def onEditedDataChanged(changedProperty : String) {}

  /**
   * Changes the data that should be edited.
   */
  def setEditedData( editedData : Data ) {
    if (currentEditedData != editedData) {
      if (currentEditedData != null) currentEditedData.removeListener( updateUi )

      currentEditedData = editedData

      if (currentEditedData != null) currentEditedData.addListener( updateUi )

      // Notify that the data changed.
      updateUi( currentEditedData, null )
    }
  }

  /**
   * The data that is currently being edited.
   */
  def editedData = currentEditedData

  /**
   * Returns the UI for this editor (creates it if necessary)
   */
  def ui = {
    if ( currentUi == null ) currentUi = createUi()

    currentUi
  }

  /**
   * Called to create an UI for this editor.
   */
  protected def createUi() : JComponent


  
}

