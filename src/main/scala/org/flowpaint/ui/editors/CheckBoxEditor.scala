package org.flowpaint.ui.editors

import _root_.org.flowpaint.property.Data
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.{JCheckBox, JComponent}
import util.Tome

/**
 *
 */

class CheckBoxEditor extends Editor {

  private var checkbox: JCheckBox = null
  private var editedParameterName: String = null
  private var checkedValue: Float = 1f
  private var uncheckedValue: Float = 0f
  private var description: String = ""

  protected def createUi(): JComponent = {

    checkbox = new javax.swing.JCheckBox()
    checkbox.setMinimumSize(new Dimension(16, 16))
    checkbox.setToolTipText(getStringProperty("description", null))
    checkbox.setText(getStringProperty("description", null))

    checkbox.addActionListener(new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent): Unit = updateParameter()
    })

    updateCheckbox()

    return checkbox

  }

  override protected def onInit() = {
    editedParameterName = getStringProperty("editedParameter", null)
    checkedValue = getFloatProperty("checkedValue", 1f)
    uncheckedValue = getFloatProperty("uncheckedValue", 0f)
    description = getStringProperty("description", editedParameterName)
  }

  private def updateCheckbox() {
    val selected = editedData.getFloatProperty(editedParameterName, uncheckedValue) == checkedValue
    checkbox.setSelected( selected )

  }

  private def updateParameter() {
    if (checkbox != null && editedParameterName != null) {

      val value = if (checkbox.isSelected) checkedValue else uncheckedValue

      editedData.setFloatProperty(editedParameterName, value)
    }

  }

}