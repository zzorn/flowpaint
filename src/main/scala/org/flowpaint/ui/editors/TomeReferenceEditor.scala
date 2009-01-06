package org.flowpaint.ui.editors

import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JComponent
import util.Tome

object NullTome extends Tome {
  def identifier = ""

  def toXML() = null


  override def toString = "None"
}

/**
 * An editor for selecting a reference to some type of tome.
 * 
 * @author Hans Haggstrom
 */
class TomeReferenceEditor extends Editor {

  private var comboBox : javax.swing.JComboBox = null

  private var updatesEnabled = true

  protected def createUi(): JComponent = {

    comboBox = new javax.swing.JComboBox()
    comboBox.setMinimumSize( new Dimension( 16, 16 ) )
    comboBox.setToolTipText( getStringProperty( "description", null ) )

    comboBox.addActionListener( new ActionListener() {
      def actionPerformed(e : java.awt.event.ActionEvent ) : Unit =
        setReference( comboBox.getSelectedItem.asInstanceOf[Tome] )
    } )

    updateSelectionFromSettings

    return comboBox
  }

  def updateSelectionFromSettings {

    if (comboBox != null && updatesEnabled) {

      if ( comboBox.getSelectedItem.asInstanceOf[Tome] != getCurrentlySelectedTome ) {

        updatesEnabled = false

        val currentTome = getCurrentlySelectedTome

        comboBox.removeAllItems
        comboBox.addItem( NullTome )
        getAvailableTomes foreach comboBox.addItem
        comboBox.setSelectedItem( currentTome )
        comboBox.repaint()

        updatesEnabled = true
      }
    }
  }

  def setReference( tome : Tome) {

    val reference = if (tome == null) "" else tome.identifier

    val editedParameterName = getStringProperty( "editedParameter", null )
    if (editedParameterName != null) editedData.setStringProperty( editedParameterName, reference )
  }

  def getTomePropertyName() : String = getStringProperty( "editedParameter", null )

  def getCurrentlySelectedTome() : Tome = {
    val editedParameterName = getTomePropertyName
    val currentValue = editedData.getStringProperty( editedParameterName, null )

    val tome = FlowPaint.library.getTome( currentValue, null.asInstanceOf[Tome] )
    
    if ( tome != null && referencedTomeType.isAssignableFrom( tome.getClass ) ) tome
    else NullTome
  }

  def referencedTomeType() : Class[Tome] = {
    val tomeTypeName = getStringProperty( "tomeType", null )

    if (tomeTypeName == null) return null

    // Get class based on class name
    val tomeType = Class.forName(tomeTypeName)

    if ( tomeType != null && ! classOf[Tome].isAssignableFrom( tomeType ) ) return null

    return tomeType.asInstanceOf[Class[Tome]]
  }

  def getAvailableTomes() : List[Tome] = {

    val tomeType = referencedTomeType()

    if (tomeType == null) Nil
    else {
      val tomes = FlowPaint.library.getTomes[Tome]( referencedTomeType() )
      tomes sort { (a : Tome, b:Tome) => a.identifier.toString < b.identifier.toString } 
    }
  }


  override def onEditedDataChanged(changedProperty: String) {

    if ( updatesEnabled &&
         ( changedProperty == null ||
           changedProperty == getTomePropertyName ) )
      updateSelectionFromSettings
  }
}