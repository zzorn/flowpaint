package org.flowpaint.ui

import java.awt.{GridLayout, FlowLayout}
import javax.swing.{BoxLayout, JPanel, JComponent}
import net.miginfocom.swing.MigLayout

/**
 * An UI panel for containing several swing components.
 * 
 * @author Hans Haggstrom
 */
class ParameterPanel {

  val ui = new JPanel()
  ui.setLayout( new MigLayout("wrap 1, fillx, insets 0","[grow]","0[]0[]0") )

  clear()

  def clear() {
    ui.removeAll()
  }

  def addUi( component: JComponent) {

    ui.add( component, "width 100%"  )

    ui.invalidate()
    ui.revalidate()
    ui.validate()

    ui.repaint()
  }

}