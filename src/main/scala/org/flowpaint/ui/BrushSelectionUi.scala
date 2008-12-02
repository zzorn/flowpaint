package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import _root_.scala.collection.jcl.ArrayList
import brush.{Brush, BrushSet}
import javax.swing.{JPanel, JLabel}
import net.miginfocom.swing.MigLayout

/**
 * Shows the brushes in some BrushSet, and updates the UI if the BrushSet was changed.
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionUi(brushSet : BrushSet) extends JPanel{

  private val brushPanel = new JPanel( new MigLayout("wrap 5, fillx, insets 0","0[]0[]0","0[]0[]0") )

  private def update() {
    brushPanel.removeAll

    brushSet.getBrushes.foreach( brush => brushPanel.add( new BrushSelectionButton(brush).ui ) )

    // Grrr.. update already
    //invalidate
    revalidate
    validate
    repaint()
  }

  setLayout(new MigLayout("wrap 1, insets 0","0[]0[]0","0[]0[]0"))

  add(new JLabel( brushSet.name ), "width 100%" )
  add( brushPanel, "width 100%" )

  update()

  brushSet.addChangeListener( update )
  
}