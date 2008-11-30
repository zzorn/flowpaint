package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import java.awt.BorderLayout
import javax.swing.{JPanel, JComponent}
import property.Data

/**
 * A trait for ui components that can be used to adjust parameters.
 *
 * @param editedData The data sample whose values should be changed when this parameter ui is tweaked by the user.
 *
 * @author Hans Haggstrom
 */
abstract class ParameterUi( editedData : Data )  {

  private var ui : JComponent = null

  final def getUi() : JComponent = {
    if (ui == null) ui = createUi()
    ui
  }

  protected def createUi() : JComponent
  
}