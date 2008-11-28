package org.flowpaint.ui

import java.awt.{GridLayout, FlowLayout}
import javax.swing.{BoxLayout, JPanel}

/**
 * An UI panel for containing several ParameterUI:s.
 * 
 * @author Hans Haggstrom
 */
class ParameterPanel {

  val ui = new JPanel()
  ui.setLayout( new FlowLayout() )


  clear()

  def clear() {
    ui.removeAll()
    ui.add( javax.swing.Box.createVerticalBox )
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