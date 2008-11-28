package org.flowpaint.ui

import java.awt.GridLayout
import javax.swing.JPanel

/**
 * An UI panel for containing several ParameterUI:s.
 * 
 * @author Hans Haggstrom
 */
class ParameterPanel {

  val ui = new JPanel(new GridLayout() )

  def clear() {
    ui.removeAll()
    
  }

  def addParameterUi( parameterUi:ParameterUi ) {

    ui.add( parameterUi )

/*
    ui.invalidate()
*/
    ui.validate()

    ui.repaint()
/*
    parameterUi.repaint()
*/
  }

}