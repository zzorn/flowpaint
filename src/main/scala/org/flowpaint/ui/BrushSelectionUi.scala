package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import _root_.scala.collection.jcl.ArrayList
import brush.Brush
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionUi(brushes : ArrayList[Brush]) extends JPanel{

  setLayout( new MigLayout("wrap 5, fillx, insets 0","0[]0[]0","0[]0[]0") )

  brushes.foreach( brush => add( new BrushSelectionButton(brush).ui ) )
}