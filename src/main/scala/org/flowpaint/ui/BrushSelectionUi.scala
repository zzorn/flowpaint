package org.flowpaint.ui

import _root_.org.flowpaint.util.DataSample
import _root_.scala.collection.jcl.ArrayList
import brush.Brush
import javax.swing.JPanel

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BrushSelectionUi(brushes : ArrayList[Brush]) extends JPanel{

  setLayout( new java.awt.GridLayout( 0,5 ) )

  brushes.foreach( brush => add( new BrushSelectionButton(brush).ui ) )
}