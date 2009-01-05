package org.flowpaint.ui.editors

import _root_.scala.swing.event.ActionEvent
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JComponent
import util.Tome

/**
 * An editor for selecting a reference to some type of tome.
 * 
 * @author Hans Haggstrom
 */

class TomeReferenceEditor extends Editor {


  protected def createUi(): JComponent = {

    val comboBox = new javax.swing.JComboBox()
    comboBox.setMinimumSize( new Dimension( 16, 16 ) )
    comboBox.setToolTipText( getStringProperty( "description", null ) )

    getAvailableTomes foreach comboBox.addItem
    comboBox.setSelectedItem( getCurrentlySelectedTome )

    comboBox.addActionListener( new ActionListener() {
      def actionPerformed(e : java.awt.event.ActionEvent ) : Unit = {
        val editedParameterName = getStringProperty( "editedParameter", null )
        if (editedParameterName != null) editedData.setStringProperty( editedParameterName, comboBox.getSelectedItem.asInstanceOf[Tome].identifier )
      }
    } )

    return comboBox
  }

  def getCurrentlySelectedTome() : Tome = {
    val editedParameterName = getStringProperty( "editedParameter", null )
    val currentValue = editedData.getStringProperty( editedParameterName, null )

    val tome = FlowPaint.library.getTome( currentValue, null.asInstanceOf[Tome] )
    
    if ( tome != null && referencedTomeType.isAssignableFrom( tome.getClass ) ) tome
    else null
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
    else FlowPaint.library.getTomes[Tome]( referencedTomeType() )
  }


}