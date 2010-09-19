package org.flowpaint.ui

import org.flowpaint.util.DataSample
import org.flowpaint.brush.{Brush, BrushSet}
import javax.swing.{JPanel, JLabel}
import net.miginfocom.swing.MigLayout

/**
 * Shows the brushes in some BrushSet, and updates the UI if the BrushSet was changed.
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionUi(brushSet : BrushSet) extends JPanel{

  private val brushPanel = new JPanel( new MigLayout("wrap 6, insets 0","0[]0[]0","0[]0[]0") )

  private def update() {
    brushPanel.removeAll

    brushSet.getBrushes.foreach( brush => brushPanel.add( new BrushSelectionButton(brush).ui ) )

    revalidate
  }

  setLayout(new MigLayout("wrap 1, fillx, insets 0","0[]0[]0","0[]0[]0"))

  add(new JLabel( brushSet.name ), "width 100%" )
  add( brushPanel, "width 100%" )

  update()

  brushSet.addChangeListener( update )
  
}